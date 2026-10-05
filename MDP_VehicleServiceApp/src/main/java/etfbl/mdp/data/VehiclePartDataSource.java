package etfbl.mdp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.models.Client;
import etfbl.mdp.models.VehiclePart;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class VehiclePartDataSource {

	public ArrayList<VehiclePart> parts = new ArrayList<>();
	private final String instanceName;
	private final JedisPool pool;
	public static VehiclePartDataSource instance = null;
	
	private VehiclePartDataSource() {
		pool = new JedisPool(Constants.REDIS_HOST);
		instanceName = Constants.REDIS_INSTANCE_NAME;
		findAll();
	}
	
	public static VehiclePartDataSource getInstance() {
		if(instance == null)
			instance = new VehiclePartDataSource();
		return instance;
	}
	
	private String partKey(String code) {
        return instanceName + ":vehiclePart:" + code;
    }
	
	public boolean save(VehiclePart part) {
		
	    try (Jedis jedis = pool.getResource()) {
	        	String key = partKey(part.getCode());
	        	if(jedis.exists(key))
	        		return false;
	            jedis.hmset(key, part.toMap());
	            parts.add(part);
	            return true;
	    }
		
    }
	
	public boolean update(VehiclePart part) {
		for(VehiclePart p : parts) {
			if(p.getCode().equals(part.getCode())) {
				 try (Jedis jedis = pool.getResource()) {
			            jedis.hmset(partKey(part.getCode()), part.toMap());
			            p = part;
			            return true;
			        }
			}
		}
		return false;
	}
	
	public VehiclePart findByCode(String code) {
	        try (Jedis jedis = pool.getResource()) {
	            Map<String, String> map = jedis.hgetAll(partKey(code));
	            return VehiclePart.fromMap(map);
	        }
	}
	
	public ArrayList<VehiclePart> findAll() {
        try (Jedis jedis = pool.getResource()) {
            
            Set<String> keys = jedis.keys(instanceName + ":vehiclePart:*");
            for (String key : keys) {
                Map<String, String> map = jedis.hgetAll(key);
                VehiclePart part = VehiclePart.fromMap(map);
                if (part != null) 
                	parts.add(part);
            }
        }
        return parts;
    }
	
	public boolean delete(String code) {
		int index = parts.indexOf(new VehiclePart(code));
		if(index >= 0) {
			parts.remove(index);
			try (Jedis jedis = pool.getResource()) {
	            jedis.del(partKey(code));
	        }
		}
		return false;
	        
	}
}
