package com.lzw.jdbc;

import java.sql.*;

public class DBUtil {
	
	//����������
	static String driver = "com.mysql.jdbc.Driver";
	//URLָ��Ҫ���ʵ����ݿ���mydata
	static String url = "jdbc:mysql://localhost:3306/serverchat?characterEncoding=UTF-8";
	//MySQL����ʱ���û���
	static String user = "root";
	//MySQL����ʱ������
	static String password = "root";
	
	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��ȡ���ݿ����Ӷ���
	static public Connection getConnection(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	//�ͷ����Ӷ���
	public static void free(ResultSet rs,Statement stmt, Connection conn){
		if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    } finally {
                        if(conn != null) {
                            try {
                                conn.close();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
		
		if(stmt != null){
        	try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
		
		if(conn != null){
        	 try {
                 conn.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             } finally {
                 try {
                     conn.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
        }
	}
	
	
	//��ȡִ�����
	public static PreparedStatement getPreparedStatement(Connection conn,String cmdText,Object... params){
		if(conn == null){
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(cmdText);
			int i=1;
			if(params!=null){
				for(Object obj:params){
					if(obj!=null){
						pstmt.setObject(i,obj);
					}
					i++;
				}
			}
			return pstmt;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DBUtil.free(null,pstmt,conn);
		}
		
		return null;
	}
	
	//ִ����ɾ�ķ���
	public static int  executeUpdate(Connection conn,String cmdText,Object...params){
		if(conn!=null){
			return -1;
		}
		PreparedStatement pstmt = getPreparedStatement(conn,cmdText,params);
		int rows = 0;
		try {
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.free(null, pstmt, conn);
        }
        return rows;
	}
	
	//ִ��������ѯ����
	public static ResultSet executeQuery(Connection conn,String cmdText,Object...params){
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, params);
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
}










