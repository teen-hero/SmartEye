package com.yan.smarteye.material.service.impl;


import com.yan.common.to.WareMaterialTo;
import com.yan.smarteye.material.dao.MaterialTypeDao;
import com.yan.smarteye.material.entity.MaterialTypeEntity;
import com.yan.smarteye.material.feign.WareMaterialFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.OnematerialDao;
import com.yan.smarteye.material.entity.OnematerialEntity;
import com.yan.smarteye.material.service.OnematerialService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("onematerialService")
public class OnematerialServiceImpl extends ServiceImpl<OnematerialDao, OnematerialEntity> implements OnematerialService {
    @Autowired
    MaterialTypeDao materialTypeDao;
    @Autowired
    WareMaterialFeignService wareMaterialFeignService;
    @Autowired
    OnematerialDao onematerialDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OnematerialEntity> page = this.page(
                new Query<OnematerialEntity>().getPage(params),
                new QueryWrapper<OnematerialEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * onematerial 区间模糊查询 检索
     * key: 华为
     * wlId: 225
     * supplierId: 2
     * min: 2
     * max: 2
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<OnematerialEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w -> w.eq("onematerial_id", key).or().like("onematerial_name", key));
        }
        // 三级id没选择不应该拼这个条件  没选应该查询所有
        String wlId = (String) params.get("wlId");
        if(!StringUtils.isEmpty(wlId) && !"0".equalsIgnoreCase(wlId)){
            wrapper.eq("wl_id", wlId);
        }
        String supplierId = (String) params.get("supplierId");
        if(!StringUtils.isEmpty(supplierId) && !"0".equalsIgnoreCase(supplierId)){
            wrapper.eq("supplier_id", supplierId);
        }
        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            // gt : 大于;  ge: 大于等于
            //TODO 真实保质期 = quality_period - （new date - creatdate） > min
            //quality_period > min + new date - creadata
            wrapper.ge("quality_period", min);
        }
        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max)){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal(min)) == 1){  //最大值比最小值小就没必要再检索了
                    // le: 小于等于
                    wrapper.le("quality_period", max);
                }
            } catch (Exception e) {
                System.out.println("OnematerialServiceImpl：前端传来非数字字符");
            }
        }

        IPage<OnematerialEntity> page = this.page(
                new Query<OnematerialEntity>().getPage(params),
                wrapper
        );
        //显示真实保质期
        for (OnematerialEntity onematerialEntity:page.getRecords()){
            if (onematerialEntity.getQualityPeriod()!=Integer.MAX_VALUE) { //排除永不过期的产品
                //此日期表示自1970年1月1日00:00:00 GMT以来的毫秒数
                long creat = onematerialEntity.getCreateTime().getTime();
                long today = new Date().getTime();
                //转为毫秒再转为天数，不足一天的按0处置
                int time = (int) ((today / 1000) / (24 * 3600) - (creat / 1000) / (24 * 3600));
                onematerialEntity.setQualityPeriod(onematerialEntity.getQualityPeriod() - time);
            }
        }
        return new PageUtils(page);
    }

    //上架onematerial，并向wms中添加warematerial库存
    @Transactional
    @Override
    public void up(Long onematerialId) {
        //1.上架onematerial
        OnematerialEntity onematerialEntity = baseMapper.selectById(onematerialId);
        onematerialEntity.setPublishStatus(1);
        baseMapper.updateById(onematerialEntity);
        //2.设置WareMaterialTo
        WareMaterialTo wareMaterialTo = new WareMaterialTo();
        BeanUtils.copyProperties(onematerialEntity,wareMaterialTo);
        MaterialTypeEntity materialTypeEntity = materialTypeDao.selectById(onematerialEntity.getMaterialTypeId());
        wareMaterialTo.setMaterialTypeName(materialTypeEntity.getMaterialTypeName());
        //3.远程调用，向wms中添加warematerial库存
        wareMaterialFeignService.add(wareMaterialTo);

    }
    /**
     * 查询materialid下的所有详情
     */
    @Override
    public List<OnematerialEntity> getByMaterialId(Long materialId) {
        QueryWrapper<OnematerialEntity> queryWrapper = new QueryWrapper<OnematerialEntity>().eq("material_id",materialId);
        List<OnematerialEntity> entities = this.baseMapper.selectList(queryWrapper);
        return entities;
    }


}