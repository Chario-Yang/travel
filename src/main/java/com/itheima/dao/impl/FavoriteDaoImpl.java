package com.itheima.dao.impl;

import com.itheima.dao.FavoriteDao;
import com.itheima.domain.Favorite;
import com.itheima.domain.Route;
import com.itheima.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public Favorite findFavorite(String rid, Integer uid) {
        Favorite favorite = null;
        try {
            favorite = jdbcTemplate.queryForObject("select * from tab_favorite where rid = ? and uid = ?", new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("找不到收藏Favorite[rid="+rid+", uid="+uid+"]");
        }
        return favorite;
    }

    @Override
    public void addFavorite(Favorite favorite, JdbcTemplate template) {
        template.update("insert into tab_favorite(rid,date,uid) values (?,?,?)", favorite.getRoute().getRid(), favorite.getDate(), favorite.getUser().getUid());
    }

    @Override
    public void updateFavoriteCount(String rid, JdbcTemplate template) {
        template.update("update tab_route set count = count + 1 where rid = ?", rid);
    }

    @Override
    public int getMyFavoriteCount(Integer uid) {
        return jdbcTemplate.queryForObject("select count(*) from tab_favorite where uid = ?", Integer.class, uid);
    }

    @Override
    public List<Route> myFavorite(Integer uid, int index, int pageSize) {
        return jdbcTemplate.query("SELECT * FROM tab_favorite f, tab_route r WHERE f.rid = r.rid AND uid = ? LIMIT ?,?", new BeanPropertyRowMapper<>(Route.class), uid, index, pageSize);
    }
}
