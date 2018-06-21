package wlmtxt;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wlmtxt.Works.dao.WorksDao;
import com.wlmtxt.Works.service.WorksService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml" })
public class MyTest {
	@Resource
	WorksService worksService;
	@Resource
	WorksDao worksDao;

	@Test
	public void ttttt() {

		System.out.println(worksService.getCollectNum("sdafasdf"));
	}

	/*
	 * 
	 */
	public WorksService getWorksService() {
		return worksService;
	}

	public WorksDao getWorksDao() {
		return worksDao;
	}

	public void setWorksDao(WorksDao worksDao) {
		this.worksDao = worksDao;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

}
