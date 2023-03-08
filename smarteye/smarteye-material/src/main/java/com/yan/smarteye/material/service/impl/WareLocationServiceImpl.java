package com.yan.smarteye.material.service.impl;

import com.yan.common.to.WareMaterialRespTo;
import com.yan.smarteye.material.vo.ListEchartsVo;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.WareLocationDao;
import com.yan.smarteye.material.entity.WareLocationEntity;
import com.yan.smarteye.material.service.WareLocationService;


@Service("wareLocationService")
public class WareLocationServiceImpl extends ServiceImpl<WareLocationDao, WareLocationEntity> implements WareLocationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareLocationEntity> page = this.page(
                new Query<WareLocationEntity>().getPage(params),
                new QueryWrapper<WareLocationEntity>()
        );

        return new PageUtils(page);
    }
    //库存分区，三级分类
    @Override
    public List<WareLocationEntity> listWithTree() {
        //1.查询出所有记录
        List<WareLocationEntity> entities = baseMapper.selectList(null);
        //2.封装成树状三级目录
        //先设置一级目录
        List<WareLocationEntity> level1Menus = new ArrayList<>();
        for (WareLocationEntity entity : entities){
            if(entity.getParentWlid()==0){
                level1Menus.add(entity);
            }
        }
        //设置一级目录的子目录
        for (WareLocationEntity leve1: level1Menus){
            leve1.setChildren(getChildrens(leve1,entities));
        }
        //排序
        level1Menus.sort(new Comparator<WareLocationEntity>() {
            @Override
            public int compare(WareLocationEntity o1, WareLocationEntity o2) {
                return (o1.getSort()==null? 0 :o1.getSort())-(o2.getSort()==null? 0:o2.getSort());
            }
        });
        return level1Menus;
    }

    //获取某菜单的子目录集合
    private List<WareLocationEntity> getChildrens(WareLocationEntity leve1, List<WareLocationEntity> all) {
        List<WareLocationEntity> children = new ArrayList<>();
        for(WareLocationEntity entity:all){
            if (entity.getParentWlid().equals(leve1.getWlId())){
                entity.setChildren(getChildrens(entity,all));   //递归设置每一层子菜单的子目录集合
                children.add(entity);
            }
        }
        //排序
        children.sort(new Comparator<WareLocationEntity>() {
            @Override
            public int compare(WareLocationEntity o1, WareLocationEntity o2) {
                return (o1.getSort()==null? 0 :o1.getSort())-(o2.getSort()==null? 0:o2.getSort());
            }
        });
        return children;
    }

    //根据catelogId查出 路径
    //找到一个三级目录对应的完整路径 [2,25,225]  /电器/好电器/手机
    @Override
    public Long[] findCatelogPath(Long wlId) {
        //新建一个ArrayList集合 接受Long
        List<Long> paths = new ArrayList<>();
        //调用方法找到完整路径[225，22，2]
        List<Long> parentPath = findParentPath(wlId, paths);
        //调用逆序方法获得正确顺序[2,25,225]
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }


    /**
     *     递归，往上找到父级菜单
     */
    private List<Long> findParentPath(Long wlId,List<Long> paths){
        //1、收集当前节点id
        paths.add(wlId);
        //2 根据当前节点ID拿到其对应的实体类
        WareLocationEntity byId = this.getById(wlId);
        //3 当实体类父id不为0继续调用这个方法
        if(byId.getParentWlid()!=0){
            findParentPath(byId.getParentWlid(),paths);
        }
        //4 跳出递归循环返回数据
        return paths;
    }

    //根据wlid、wlpid、wlppid查并设置对应名称
    @Override
    public List<WareMaterialRespTo> transform(List<WareMaterialRespTo> tos) {
        for (WareMaterialRespTo to : tos){
            to.setWlName(baseMapper.selectById(to.getWlId()).getName());
            to.setWlpName(baseMapper.selectById(to.getWlpId()).getName());
            to.setWlppName(baseMapper.selectById(to.getWlppId()).getName());
        }
        return tos;
    }

    /**
     * 库存分区echarts
     */
    @Override
    public List<ListEchartsVo> listEcharts() {
        //先拿到全部tree
        List<WareLocationEntity> wareLocationEntities = listWithTree();
        //设置第一层
        List<ListEchartsVo> vos = new ArrayList<>();
        for (WareLocationEntity entity:wareLocationEntities){
            //设置vo的name
            ListEchartsVo vo = new ListEchartsVo();
            vo.setName(entity.getName());
            //第一层值设置大一点
            vo.setValue(10);
            //传入temp方法，设置子孙
            vo.setChildren(temp(entity.getChildren()));
            vos.add(vo);
        }
        return vos;
    }
    //设置子孙
    public List<ListEchartsVo> temp(List<WareLocationEntity> wareLocationEntities){

        List<ListEchartsVo> vos = new ArrayList<>();
        for (WareLocationEntity entity:wareLocationEntities){
            //设置vo的name
            ListEchartsVo vo = new ListEchartsVo();
            vo.setName(entity.getName());
            //设置vo的children
            if (entity.getChildren()!=null && entity.getChildren().size()!=0){
                vo.setChildren(temp(entity.getChildren()));
            }
            vos.add(vo);
        }
        return vos;
    }
}