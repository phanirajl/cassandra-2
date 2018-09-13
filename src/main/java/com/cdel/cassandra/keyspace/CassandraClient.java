package com.cdel.cassandra.keyspace;

import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.google.common.base.Preconditions;

/**cassandra客户端
 * @author DELL
 *
 */
public class CassandraClient {

	/**
     * cluster
     */
    private static Cluster cluster;

    /**
     * session
     */
    private static Session session;

    /**
     * table name
     */
    private String         keyspace;

    /**
     * server ip
     */
    private String[] serverIps = new String[]{"192.168.170.104", "192.168.170.105", "192.168.170.106"};

    public CassandraClient(){
    }

    public CassandraClient(String keyspace){
        this.keyspace = keyspace;
        createClient();
    }

	public CassandraClient(String keyspace, String[] serverIps){
		this.keyspace = keyspace;
		this.serverIps = serverIps;
		createClient();
	}

    /**
     * 创建cassandra客户端
     */
    public void createClient() {
    	Preconditions.checkNotNull(keyspace, "keyspace must not be null.");
        Preconditions.checkNotNull(serverIps, "serverIp must not be null.");

        //使用Cluster.Builder对象的addContactPoint（）方法添加联系点（节点的IP地址）。此方法返回Cluster.Builder。
        Cluster.Builder builder = Cluster.builder().addContactPoints(serverIps);

        // 设置连接池
        PoolingOptions poolingOptions = getPoolingOptions();
        builder.withPoolingOptions(poolingOptions);

        // socket 链接配置
        SocketOptions socketOptions = new SocketOptions().setKeepAlive(true)
        		.setReceiveBufferSize(1024 * 1024)
        		.setSendBufferSize(1024 * 1024)
        		.setConnectTimeoutMillis(5 * 1000)
        		.setReadTimeoutMillis(1000);

        builder.withSocketOptions(socketOptions);

        // 设置压缩方式
        builder.withCompression(ProtocolOptions.Compression.LZ4);

        // 负载策略
        DCAwareRoundRobinPolicy dCAwareRoundRobinPolicy = DCAwareRoundRobinPolicy.builder().withLocalDc("myLocalDC").withUsedHostsPerRemoteDc(2).allowRemoteDCsForLocalConsistencyLevel().build();
        builder.withLoadBalancingPolicy(dCAwareRoundRobinPolicy);

        // 重试策略
        builder.withRetryPolicy(DefaultRetryPolicy.INSTANCE);

        //使用新的构建器对象，创建一个集群对象。为此，在Cluster.Builder类中有一个名为build（）的方法。以下代码显示如何创建集群对象。
        cluster = builder.build();
        //使用Cluster类的connect（）方法创建一个Session对象的实例，如下所示。
        //此方法创建一个新会话并初始化它。如果已经有一个键空间，可以通过将字符串格式的键空间名称传递给这个方法来将其设置为现有键空间，如下所示。
        session = cluster.connect(keyspace);

        Preconditions.checkNotNull(session, "session must not be null.");
    }

    /**
     * 构建连接池
     *
     * @return 连接池
     */
    private PoolingOptions getPoolingOptions() {
        PoolingOptions poolingOptions = new PoolingOptions().setCoreConnectionsPerHost(HostDistance.LOCAL, 4)
        		.setMaxConnectionsPerHost(HostDistance.LOCAL, 10)
        		.setCoreConnectionsPerHost(HostDistance.REMOTE, 2)
        		.setMaxConnectionsPerHost(HostDistance.REMOTE, 20);
        return poolingOptions;
    }

    /**
     * 执行预处理sql
     *
     * @param sql 数据库语句
     * @param params 占位符值
     * @return 执行结果
     */
    public ResultSet executePreparedSQL(String sql, List<Object> params) {

        Preconditions.checkNotNull(session, "session must not be null.");
        Preconditions.checkNotNull(sql, "sql must not be null.");
        Preconditions.checkNotNull(params, "params must not be null.");

        PreparedStatement prepared = session.prepare(sql);
        BoundStatement bound = prepared.bind(params.toArray());
        return session.execute(bound);
    }

    /**
     * 执行sql语句
     *
     * @param sql 数据库语句
     * @return 执行结果
     */
    public ResultSet executeSQL(String sql) {

        Preconditions.checkNotNull(session, "session must not be null.");
        Preconditions.checkNotNull(sql, "sql must not be null.");

        return session.execute(sql);
    }

    public void close() {

        if (session != null) {
            session.close();
        }

        if (null != cluster) {
            cluster.close();
        }

    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

	public String[] getServerIps() {
		return serverIps;
	}

	public void setServerIps(String[] serverIps) {
		this.serverIps = serverIps;
	}

}
