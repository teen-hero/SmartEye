package com.yan.smarteye.material.service.impl;

import com.yan.smarteye.material.dao.MaterialTypeShelfRelationDao;
import com.yan.smarteye.material.entity.MaterialTypeEntity;
import com.yan.smarteye.material.service.MaterialTypeService;
import com.yan.smarteye.material.vo.ShelfWithMaterialTypeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.ShelfDao;
import com.yan.smarteye.material.entity.ShelfEntity;
import com.yan.smarteye.material.service.ShelfService;


@Service("shelfService")
public class ShelfServiceImpl extends ServiceImpl<ShelfDao, ShelfEntity> implements ShelfService {
    @Autowired
    MaterialTypeShelfRelationDao materialTypeShelfRelationDao;
    @Autowired
    MaterialTypeService materialTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShelfEntity> page = this.page(
                new Query<ShelfEntity>().getPage(params),
                new QueryWrapper<ShelfEntity>()
        );

        return new PageUtils(page);
    }

    //根据三级目录id查出其下的数据并分页查询，并支持查找（如果参数中有就查找，没有就不查），0查全部
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long wlId) {
        //1 首先拿到传过来参数中的key对应的参数
        String key = (String) params.get("key");
        //select * from pms_attr_group where catelog_id=? and (attr_group_id=key or attr_group_name like %key%)

        //2 创造一个对应实体类的queryWrapper
        QueryWrapper<ShelfEntity> wrapper = new QueryWrapper<ShelfEntity>();

        //3 如果key关键字对应的不为空的话
        if(key!=null){
            //要满足and (attr_group_id=key or attr_group_name like %key%)
            wrapper.and((obj)->{
                obj.eq("shelf_id",key).or().like("shelf_name",key);
            });
        }

        //4 如果传来的wlId为0
        if( wlId == 0){

            //直接根据params和wrapper生成page
            //Query utils不过是把params 变为 page
            IPage<ShelfEntity> page = this.page(new Query<ShelfEntity>().getPage(params),
                    wrapper);

            //把Ipage对象通过PageUtils进行处理 处理成我们自定义的字段返回给前端
            return new PageUtils(page);
        }else {

            // 5 如果传来的cateLogId不为0 wrapper再次匹配这个字段
            //Query utils不过是把params 变为 page
            wrapper.eq("wl_id",wlId);
            IPage<ShelfEntity> page = this.page(new Query<ShelfEntity>().getPage(params), wrapper);
            //把Ipage对象通过PageUtils进行处理 处理成我们自定义的字段返回给前端
            return new PageUtils(page);
        }
    }

    //根据分区id查出所有的货架以及这些货架里面的库存种类
    @Override
    public List<ShelfWithMaterialTypeVo> getShelfWithMaterialTypeBywlId(Long wlId) {
        //1、查询分组信息(列出所有与目录ID相关的分组)
        List<ShelfEntity> shelfEntities = this.list(new QueryWrapper<ShelfEntity>().eq("wl_id", wlId));
        //2、查询所有属性
        List<ShelfWithMaterialTypeVo> collect = shelfEntities.stream().map(group -> {
            //创建一个返回VO
            ShelfWithMaterialTypeVo shelfWithMaterialTypeVo = new ShelfWithMaterialTypeVo();
            //copy属性
            BeanUtils.copyProperties(group,shelfWithMaterialTypeVo);
            //根据分组ID得到对应的属性函数
            List<MaterialTypeEntity> materialTypeEntities = materialTypeService.getRelationMaterialType(shelfWithMaterialTypeVo.getShelfId());
            //把得到的对应属性赋值到VO中
            shelfWithMaterialTypeVo.setMaterialTypeEntities(materialTypeEntities);
            return shelfWithMaterialTypeVo;
        }).collect(Collectors.toList());

        return collect;
    }


}