package com.yan.smarteye.material.service.impl;

import com.mysql.cj.util.StringUtils;
import com.yan.smarteye.material.dao.OnematerialDao;
import com.yan.smarteye.material.entity.OnematerialEntity;
import com.yan.smarteye.material.service.WareLocationService;
import com.yan.smarteye.material.vo.saveVo.OneMaterialVo;
import com.yan.smarteye.material.vo.saveVo.MaterialVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.MaterialDao;
import com.yan.smarteye.material.entity.MaterialEntity;
import com.yan.smarteye.material.service.MaterialService;
import org.springframework.transaction.annotation.Transactional;


@Service("materialService")
public class MaterialServiceImpl extends ServiceImpl<MaterialDao, MaterialEntity> implements MaterialService {
    @Autowired
    OnematerialDao onematerialDao;
    @Autowired
    WareLocationService wareLocationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MaterialEntity> page = this.page(
                new Query<MaterialEntity>().getPage(params),
                new QueryWrapper<MaterialEntity>()
        );

        return new PageUtils(page);
    }

    //保存material及onematerial,入库操作
    @Transactional
    @Override
    public void saveInfo(MaterialVo materialVo) {
        //1、保存material基本信息`gms_material`
        MaterialEntity materialEntity = new MaterialEntity();
        BeanUtils.copyProperties(materialVo,materialEntity);
        //保存material的描述图片  以，为分割拼接字符串
        List<String> images = materialVo.getMaterialImageInfo();
        materialEntity.setMaterialImage(String.join(",",images));
        //上架状态默认设置为0
        materialEntity.setPublishStatus(0);
        //时间设置也可以通过mp在实体类标注时间字段的自动填充实现
        materialEntity.setCreateTime(new Date());
        materialEntity.setUpdateTime(new Date());
        this.save(materialEntity);
        //2.保存onematerial的信息`gms_onematerial`
        for (OneMaterialVo oneMaterial : materialVo.getOneMaterial()){
            OnematerialEntity onematerialEntity = new OnematerialEntity();
            BeanUtils.copyProperties(oneMaterial,onematerialEntity);
            //如果没设置保质期，则说明不会过期，设为最大值
            if (oneMaterial.getQualityPeriod()==null){
                onematerialEntity.setQualityPeriod(Integer.MAX_VALUE);
            }
            //设置对应的material的Id
            onematerialEntity.setMaterialId(materialEntity.getMaterialId());
            //设置wlId、supplierId
            onematerialEntity.setSupplierId(materialEntity.getSupplierId());
            onematerialEntity.setWlId(materialEntity.getWlId());
            //设置wlpId、wlppId
            Long[] catelogPath = wareLocationService.findCatelogPath(materialEntity.getWlId());
            onematerialEntity.setWlpId(catelogPath[1]);
            onematerialEntity.setWlppId(catelogPath[0]);
            //上架状态默认设置为0
            onematerialEntity.setPublishStatus(0);
            //时间设置也可以通过mp在实体类标注时间字段的自动填充实现
            onematerialEntity.setCreateTime(new Date());
            onematerialEntity.setUpdateTime(new Date());
            onematerialDao.insert(onematerialEntity);
        }
    }

    //检索查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<MaterialEntity> wrapper = new QueryWrapper<>();

        String wlId = (String) params.get("wlId");
        if (!StringUtils.isNullOrEmpty(wlId) && !"0".equalsIgnoreCase(wlId)){
            wrapper.eq("wl_id", wlId);
        }

        String supplierId = (String) params.get("supplierId");
        if (!StringUtils.isNullOrEmpty(supplierId) && !"0".equalsIgnoreCase(supplierId)){
            wrapper.eq("supplier_id", supplierId);
        }

        String status = (String) params.get("status");
        if (!StringUtils.isNullOrEmpty(status)){
            wrapper.eq("publish_status", status);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)){
            wrapper.and((w) -> {
                w.eq("material_id", key).or().like("material_name", key);
            });
        }

        IPage<MaterialEntity> page = this.page(
                new Query<MaterialEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    //上架material，返回0代表onematerial没全部上架material上架失败,1代表material上架成功
    @Override
    public int up(Long materialId) {
        //先判断其包含的onematerial是否全部上架，全部上架才可进行material上架
        List<OnematerialEntity> onematerialEntities = onematerialDao.selectList(new QueryWrapper<OnematerialEntity>().eq("material_id", materialId));
        for (OnematerialEntity onematerialEntity:onematerialEntities){
            if (onematerialEntity.getPublishStatus()==0){  //只要onematerial存在未上架的，就返回0
                return 0; //上架material失败
            }
        }
        //上架material
        MaterialEntity materialEntity = baseMapper.selectById(materialId);
        materialEntity.setPublishStatus(1);
        baseMapper.updateById(materialEntity);
        return 1;  //上架material成功
    }

}