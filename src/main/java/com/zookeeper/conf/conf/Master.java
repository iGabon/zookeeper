package com.zookeeper.conf.conf;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Master implements Watcher{
	
	private static final Logger logger = LoggerFactory.getLogger(Master.class);
	
	private boolean isLeader = false;
	
	ZooKeeper zk;
	
	String serverId = Integer.toHexString(new Random().nextInt());
	
	void startZK() throws IOException{
		zk = new ZooKeeper("59.111.126.208:2181", 6000, new Master());
	}
	
	void runForMaster(){
		while (true) {
			try {
				zk.create(Const.MASTER, serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} catch (KeeperException e) {
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ZooKeeper zk = new ZooKeeper("59.111.126.208:2181", 6000, new Master());
			try {
				if (zk.exists(Const.MASTER, true) == null) {
					zk.create(Const.MASTER, "master process".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				System.out.println("/master is already exist");
				zk.setData(Const.MASTER, "master changed".getBytes(), 1);
			} catch (KeeperException e) {
				logger.debug("{}", e.getMessage());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			logger.info("connection timeout.{}", e);
		}
	}

	public void process(WatchedEvent event) {
		System.out.println("master changed");
	}

}
