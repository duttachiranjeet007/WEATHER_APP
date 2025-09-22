// LocationService.java
package weather;

import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LocationService {
    
    public static LocationData getCurrentLocation() {
        try {
            URL url = new URL(Config.IP_LOCATION_URL);
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

            JSONObject jsonObject = new JSONObject(informationString.toString());
            
            LocationData location = new LocationData();
            location.setCity(jsonObject.getString("city"));
            location.setCountry(jsonObject.getString("country"));
            location.setLat(jsonObject.getDouble("lat"));
            location.setLon(jsonObject.getDouble("lon"));
            location.setRegion(jsonObject.getString("regionName"));
            
            return location;

        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultLocation();
        }
    }

    private static LocationData getDefaultLocation() {
        LocationData defaultLocation = new LocationData();
        defaultLocation.setCity("London");
        defaultLocation.setCountry("UK");
        defaultLocation.setLat(51.5074);
        defaultLocation.setLon(-0.1278);
        return defaultLocation;
    }

    public static class LocationData {
        private String city;
        private String country;
        private String region;
        private double lat;
        private double lon;

        // Getters and Setters
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }

        public double getLon() { return lon; }
        public void setLon(double lon) { this.lon = lon; }
    }
}