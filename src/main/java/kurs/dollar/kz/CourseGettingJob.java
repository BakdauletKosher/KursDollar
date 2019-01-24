package kurs.dollar.kz;

import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by BahaWood on 1/24/19.
 */
@Component
public class CourseGettingJob extends QuartzJobBean {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final String url = "http://www.apilayer.net/api/live?access_key=bc5c2d1a23b8cfd97c77e0aaeb896221&format=1";
        try{
            //GET COURSE OF DOLLAR FROM apilayer.net through API (GET REQUEST)
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject myResponse = new JSONObject(response.toString());
            String usd = myResponse.getJSONObject("quotes").get("USDKZT").toString();

            CourseModel model = new CourseModel();
            model.setDate(new Date());
            model.setRate(Float.parseFloat(usd));
            courseRepository.save(model);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


}
