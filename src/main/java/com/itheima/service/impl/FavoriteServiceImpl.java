package com.itheima.service.impl;

import com.itheima.dao.FavoriteDao;
import com.itheima.domain.Favorite;
import com.itheima.domain.PageBean;
import com.itheima.domain.Route;
import com.itheima.domain.User;
import com.itheima.service.FavoriteService;
import com.itheima.util.BeanFactory;
import com.itheima.util.JdbcUtils;
import com.itheima.util.PageUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    //private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private FavoriteDao favoriteDao = (FavoriteDao) BeanFactory.getBean("favoriteDao");

    @Override
    public boolean isFavorite(String rid, User user) {
        Favorite favorite = favoriteDao.findFavorite(rid,user.getUid());
        return favorite!=null;
    }

    @Override
    public boolean addFavorite(String rid, User user) {
        //把收藏信息，封装成一个Route对象（放Servlet里封装也可以）
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        favorite.setDate(format.format(new Date()));
        Route route = new Route();
        route.setRid(Integer.parseInt(rid));
        favorite.setRoute(route);


        //1.得到连接池
        DataSource dataSource = JdbcUtils.getDataSource();
        //2.创建一个JDBCTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //3.使用Spring的工具类，开启   事务的线程标志
        TransactionSynchronizationManager.initSynchronization();
        //4.使用Spring的工具类，从连接池获取一个连接对象
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            //5.开启事务
            connection.setAutoCommit(false);
            //6.调用dao，插入收藏的记录
            favoriteDao.addFavorite(favorite, jdbcTemplate);

            //int i = 1/0;

            //7.调用dao，修改收藏数量
            favoriteDao.updateFavoriteCount(rid, jdbcTemplate);
            //8.关闭事务：提交
            connection.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //8.关闭事务：回滚
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            //9.重置连接的autoCommit为true
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //10.清理线程标志
            TransactionSynchronizationManager.clearSynchronization();
        }

        return false;
    }

    @Override
    public PageBean<Route> myFavorite(User user, int pageNumber, int pageSize) {
        PageBean<Route> pageBean = new PageBean<>();
        /*当前页码 */
        pageBean.setPageNumber(pageNumber);
        /*每页多少条*/
        pageBean.setPageSize(pageSize);
        /*总共多少数据*/
        int totalCount = favoriteDao.getMyFavoriteCount(user.getUid());
        pageBean.setTotalCount(totalCount);
        /*分了多少页*/
        int pageCount = PageUtils.calcPageCount(totalCount, pageSize);
        pageBean.setPageCount(pageCount);
        /*页码条从几开始显示*/
        int[] pagination = PageUtils.pagination(pageNumber, pageCount);
        pageBean.setStart(pagination[0]);
        /*页码条显示到几结束*/
        pageBean.setEnd(pagination[1]);
        /*当前页的数据集合*/
        int index = PageUtils.calcSqlLimitIndex(pageNumber, pageSize);
        List<Route> routeList =  favoriteDao.myFavorite(user.getUid(), index, pageSize);
        pageBean.setData(routeList);

        return pageBean;
    }
}
