package com.itheima.dao.impl;

import com.itheima.dao.RouteDao;
import com.itheima.domain.*;
import com.itheima.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public List<Route> hotRoutes() {
        return jdbcTemplate.query("SELECT * FROM tab_route WHERE rflag = 1 ORDER BY COUNT DESC LIMIT 0, 4", new BeanPropertyRowMapper<>(Route.class));
    }

    @Override
    public List<Route> newRoutes() {
        return jdbcTemplate.query("SELECT * FROM tab_route WHERE rflag = 1 ORDER BY rdate DESC LIMIT 0, 4",new BeanPropertyRowMapper<>(Route.class));
    }

    @Override
    public List<Route> themeRoutes() {
        return jdbcTemplate.query("SELECT * FROM tab_route WHERE rflag = 1 AND isThemeTour = 1 LIMIT 0, 4",new BeanPropertyRowMapper<>(Route.class));
    }

    /**
     * 问题：
     *  参数cid没有值，但是SQL里有cid的条件。
     * 解决：根据参数值，动态拼接SQL语句
     *  判断cid是否有值。如果有值，SQL就增加cid的条件；如果没有值，sql就不增加cid的条件
     *  判断rname是否有值。如果有值，SQL就增加rname的条件；如果没有值，sql就不增加rname的条件
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int getTotalCount(String cid, String rname) {
        String sql = "select count(*) from tab_route where rflag = 1 ";
        List<Object> params = new ArrayList<>();
        if (cid != null && !"".equals(cid)) {
            sql += " and cid = ?";
            params.add(cid);
        }
        if (rname != null && !"".equals(rname)) {
            sql += " and rname like ?";
            params.add("%" + rname + "%");
        }
        return jdbcTemplate.queryForObject(sql,Integer.class, params.toArray());
    }

    @Override
    public List<Route> search(String cid, String rname, int index, int pageSize) {
        String sql = "select * from tab_route where rflag = 1 ";
        List<Object> params = new ArrayList<>();
        if (cid != null && !"".equals(cid)) {
            sql += " and cid = ? ";
            params.add(cid);
        }
        if (rname != null && !"".equals(rname)) {
            sql += " and rname like ? ";
            params.add("%" + rname + "%");
        }
        sql += "limit ?,?";
        params.add(index);
        params.add(pageSize);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }

    @Override
    public Route findById(String rid) {
        Route route = null;
        try {
            route = jdbcTemplate.queryForObject("select * from tab_route where rid = ?", new BeanPropertyRowMapper<>(Route.class), rid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到旅游路线信息Route[rid="+rid+"]");
        }
        return route;
    }

    @Override
    public Seller findSellerById(Integer sid) {
        Seller seller = null;
        try {
            seller = jdbcTemplate.queryForObject("select * from tab_seller where sid = ?", new BeanPropertyRowMapper<>(Seller.class), sid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到商家信息Seller[sid="+sid+"]");
        }
        return seller;
    }

    @Override
    public Category findCategoryById(Integer cid) {
        Category category = null;
        try {
            category = jdbcTemplate.queryForObject("select * from tab_category where cid = ?", new BeanPropertyRowMapper<>(Category.class), cid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到分类信息Category[cid="+cid+"]");
        }
        return category;
    }

    @Override
    public List<RouteImg> findImages(String rid) {
        return jdbcTemplate.query("select * from tab_route_img where rid = ?", new BeanPropertyRowMapper<>(RouteImg.class), rid);
    }

    @Override
    public Map<String, Object> findById2(String rid) {
        return jdbcTemplate.queryForMap("SELECT * FROM tab_route r, tab_category c, tab_seller s WHERE r.cid=c.cid AND r.sid=s.sid AND rid = ?", rid);
    }

    @Override
    public int getTotalCount2(QueryVO vo) {
        //SELECT COUNT(*) FROM tab_route WHERE rflag = 1 AND rname LIKE ? AND price >= ? AND price <= ?
        String sql = "SELECT COUNT(*) FROM tab_route WHERE rflag = 1 ";
        List<Object> params = new ArrayList<>();
        if (vo.getRname() != null && !"".equals(vo.getRname())) {
            sql += " and rname like ?";
            params.add("%" + vo.getRname() + "%");
        }
        if (vo.getMinprice() != null && !"".equals(vo.getMinprice())) {
            sql += " and price >= ?";
            params.add(vo.getMinprice());
        }
        if (vo.getMaxprice() != null&& !"".equals(vo.getMaxprice())) {
            sql += " and price <= ?";
            params.add(vo.getMaxprice());
        }
        return jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> rankRoutes(QueryVO vo, int index, int pageSize) {
        //SELECT * FROM tab_route WHERE rflag = 1 AND rname LIKE ? AND price >= ? AND price <= ? ORDER BY COUNT DESC LIMIT ?,?
        String sql = "SELECT * FROM tab_route WHERE rflag = 1 ";
        List<Object> params = new ArrayList<>();
        if (vo.getRname() != null && !"".equals(vo.getRname())) {
            sql += " and rname like ?";
            params.add("%" + vo.getRname() + "%");
        }
        if (vo.getMinprice() != null && !"".equals(vo.getMinprice())) {
            sql += " and price >= ?";
            params.add(vo.getMinprice());
        }
        if (vo.getMaxprice() != null&& !"".equals(vo.getMaxprice())) {
            sql += " and price <= ?";
            params.add(vo.getMaxprice());
        }
        sql += " ORDER BY COUNT DESC LIMIT ?,?";
        params.add(index);
        params.add(pageSize);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }
}
