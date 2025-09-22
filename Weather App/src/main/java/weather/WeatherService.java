// WeatherService.java
package weather;

import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {
    
    public static WeatherData getWeatherData(double lat, double lon) {
        try {
            String urlString = Config.WEATHER_API_URL + 
                "?lat=" + lat + "&lon=" + lon + "&appid=" + Config.API_KEY;
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            return parseWeatherData(informationString.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WeatherData getWeatherData(String cityName) {
        try {
            String urlString = Config.WEATHER_API_URL + 
                "?q=" + cityName + "&appid=" + Config.API_KEY;
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();

            return parseWeatherData(informationString.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static WeatherData parseWeatherData(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        
        WeatherData weatherData = new WeatherData();
        
        // Basic location info
        weatherData.setCityName(jsonObject.getString("name"));
        weatherData.setCountry(jsonObject.getJSONObject("sys").getString("country"));
        
        // Coordinates
        weatherData.setLatitude(jsonObject.getJSONObject("coord").getDouble("lat"));
        weatherData.setLongitude(jsonObject.getJSONObject("coord").getDouble("lon"));
        
        // Main weather data
        JSONObject main = jsonObject.getJSONObject("main");
        weatherData.setTemperature(main.getDouble("temp"));
        weatherData.setFeelsLike(main.getDouble("feels_like"));
        weatherData.setPressure(main.getDouble("pressure"));
        weatherData.setHumidity(main.getInt("humidity"));
        
        // Wind
        weatherData.setWindSpeed(jsonObject.getJSONObject("wind").getDouble("speed"));
        
        // Weather description
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        weatherData.setDescription(weather.getString("description"));
        weatherData.setIcon(weather.getString("icon"));
        
        // Timestamp
        weatherData.setTimestamp(jsonObject.getLong("dt"));
        
        return weatherData;
    }
}