// WeatherData.java
package weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class WeatherData {
    private String cityName;
    private String country;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private String description;
    private String icon;
    private long timestamp;
    private double latitude;
    private double longitude;

    // Getters and Setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public LocalDateTime getDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    public double getTemperatureCelsius() {
        return temperature - 273.15;
    }

    public double getTemperatureFahrenheit() {
        return (temperature - 273.15) * 9/5 + 32;
    }

    @Override
    public String toString() {
        return String.format("""
            Weather in %s, %s:
            Temperature: %.1f°C (Feels like: %.1f°C)
            Conditions: %s
            Humidity: %d%%
            Pressure: %.1f hPa
            Wind Speed: %.1f m/s
            Last Updated: %s
            """, cityName, country, getTemperatureCelsius(), 
            feelsLike - 273.15, description, humidity, pressure, 
            windSpeed, getDateTime());
    }
}