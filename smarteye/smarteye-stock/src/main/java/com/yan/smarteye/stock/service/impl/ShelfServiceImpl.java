package com.yan.smarteye.stock.service.impl;

import com.yan.smarteye.stock.dao.StockTypeShelfRelationDao;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.service.StockTypeService;
import com.yan.smarteye.stock.vo.ShelfWithStockTypeVo;
import com.yan.smarteye.stock.vo.UpShelfInfoResp;
import com.yan.smarteye.stock.vo.UpShelfInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.ShelfDao;
import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.service.ShelfService;


@Service("shelfService")
public class ShelfServiceImpl extends ServiceImpl<ShelfDao, ShelfEntity> implements ShelfService {
    @Autowired
    StockTypeShelfRelationDao stockTypeShelfRelationDao;
    @Autowired
    StockTypeService stockTypeService;

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

        //2 创造一个对应实体类的queryWrapper
        QueryWrapper<ShelfEntity> wrapper = new QueryWrapper<ShelfEntity>();

        //3 如果key关键字对应的不为空的话
        if(key!=null){

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

            // 5 如果传来的wlId不为0 wrapper再次匹配这个字段
            wrapper.eq("wl_id",wlId);
            IPage<ShelfEntity> page = this.page(new Query<ShelfEntity>().getPage(params), wrapper);
            //把Ipage对象通过PageUtils进行处理 处理成我们自定义的字段返回给前端
            return new PageUtils(page);
        }
    }

    //根据分区id查出所有的货架以及这些货架里面的库存种类
    @Override
    public List<ShelfWithStockTypeVo> getShelfWithStockTypeBywlId(Long wlId) {
        //1、查询分组信息(列出所有与目录ID相关的分组)
        List<ShelfEntity> shelfEntities = this.list(new QueryWrapper<ShelfEntity>().eq("wl_id", wlId));
        //2、查询所有属性
        List<ShelfWithStockTypeVo> collect = shelfEntities.stream().map(group -> {
            //创建一个返回VO
            ShelfWithStockTypeVo shelfWithStockTypeVo = new ShelfWithStockTypeVo();
            //copy属性
            BeanUtils.copyProperties(group,shelfWithStockTypeVo);
            //根据分组ID得到对应的属性函数
            List<StockTypeEntity> stockTypeEntities = stockTypeService.getRelationStockType(shelfWithStockTypeVo.getShelfId());
            //把得到的对应属性赋值到VO中
            shelfWithStockTypeVo.setStockTypeEntities(stockTypeEntities);
            return shelfWithStockTypeVo;
        }).collect(Collectors.toList());

        return collect;
    }

    //查询wlId下的货架的上架情况（onestock）
    //该方法涉及到可视化展示，使用频繁，加缓存,所有wlid只要用到了就都是热点，全部加缓存
    //考虑到大屏一分钟刷新更新一次，所有缓存存活时间设置为59s
    @Cacheable(value = "shelf",key = "#wlId")
    @Override
    public List<UpShelfInfoResp> queryUpshelf(Long wlId) {
        //wlid下的全部货架
        QueryWrapper<ShelfEntity> wrapper = new QueryWrapper<ShelfEntity>().eq("wl_id", wlId);
        List<ShelfEntity> shelfEntities = baseMapper.selectList(wrapper);
        List<UpShelfInfoResp> res = new ArrayList<>();
        for(ShelfEntity shelfEntity : shelfEntities) {
            UpShelfInfoResp upShelfInfoResp = new UpShelfInfoResp();
            BeanUtils.copyProperties(shelfEntity, upShelfInfoResp);
            res.add(upShelfInfoResp);
        }
        //有货的货架
        List<UpShelfInfoResp> upres = baseMapper.queryUpshelfInfo(wlId);
        //给有货的货架设置数量
        for(UpShelfInfoResp u : upres) {
            for (UpShelfInfoResp upShelfInfoResp : res) {
                if (upShelfInfoResp.getShelfName().equals(u.getShelfName())) {
                    upShelfInfoResp.setCount(u.getCount());
                }
            }
        }
        return res;
    }

    //查询wlId下的货架的上架情况（onestock）详情 指的是某货架上放了什么
    @Override
    public List<UpShelfInfoVo> queryUpshelfInfo(Long wlId, String shelfName) {
        List<UpShelfInfoVo> res = baseMapper.queryUpshelfInfodetial(wlId,shelfName);
        return res;
    }


}