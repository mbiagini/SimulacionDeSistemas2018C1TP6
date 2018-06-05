package service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import service.FileService;
import service.impl.FileServiceImpl;
import persistence.granular.GranularParticle;
import persistence.granular.Parameters;
import service.TestService;

public class TestServiceImpl implements TestService {
	
	private static FileService fileServiceTP4 = new FileServiceImpl();
	
	@Override
	public void generateParticles(Parameters params, String filename) {
		
		List<GranularParticle> particles = new ArrayList<>();
		
		GranularParticle p1 = new GranularParticle.Builder()
				.id(1)
				.x(10.0)
				.y(10.0)
				.r(params.getMinRadius())
				.m(params.getMass())
				.v(0.0)
				.ang(0)
				.acc(0.0)
				.build();
		GranularParticle p2 = new GranularParticle.Builder()
				.id(2)
				.x(10.0)
				.y(11.2)
				.r(params.getMinRadius())
				.m(params.getMass())
				.v(0.0)
				.ang(0.0)
				.acc(0.0)
				.build();
//		GranularParticle p3 = new GranularParticle.Builder()
//				.id(3)
//				.x(0.1)
//				.y(0.1)
//				.r(params.getMinRadius())
//				.m(params.getMass())
//				.v(0.0)
//				.ang(0)
//				.acc(0.0)
//				.build();
//		GranularParticle p4 = new GranularParticle.Builder()
//				.id(4)
//				.x(0.1)
//				.y(0.14)
//				.r(params.getMinRadius())
//				.m(params.getMass())
//				.v(0.0)
//				.ang(0)
//				.acc(0.0)
//				.build();
		
		particles.add(p1);
		particles.add(p2);
//		particles.add(p3);
//		particles.add(p4);
		
		Gson gson = new Gson();
		String json = gson.toJson(particles);
		
		File file = new File(filename);
		fileServiceTP4.writeStringToFile(file, json);
	}

}
