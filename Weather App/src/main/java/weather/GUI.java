// GUI.java
package weather;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUI extends JFrame {
    private JTextField cityField;
    private JTextArea weatherArea;
    private JButton searchButton;
    private JButton locationButton;
    private JLabel statusLabel;

    public GUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Weather Forecast Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        createTopPanel();
        createCenterPanel();
        createBottomPanel();

        setVisible(true);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField(15);
        
        searchButton = new JButton("Search");
        locationButton = new JButton("Use My Location");
        
        topPanel.add(cityLabel);
        topPanel.add(cityField);
        topPanel.add(searchButton);
        topPanel.add(locationButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Add action listeners
        searchButton.addActionListener(new SearchAction());
        locationButton.addActionListener(new LocationAction());
    }

    private void createCenterPanel() {
        weatherArea = new JTextArea();
        weatherArea.setEditable(false);
        weatherArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        weatherArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(weatherArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        statusLabel = new JLabel("Ready to fetch weather data...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                fetchWeatherByCity(city);
            } else {
                JOptionPane.showMessageDialog(GUI.this, 
                    "Please enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LocationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fetchWeatherByLocation();
        }
    }

    private void fetchWeatherByCity(String city) {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Fetching weather data for " + city + "...");
                    searchButton.setEnabled(false);
                    locationButton.setEnabled(false);
                });

                WeatherData weatherData = WeatherService.getWeatherData(city);
                
                SwingUtilities.invokeLater(() -> {
                    if (weatherData != null) {
                        displayWeatherData(weatherData);
                        statusLabel.setText("Weather data loaded successfully");
                    } else {
                        weatherArea.setText("Error: Could not fetch weather data for " + city);
                        statusLabel.setText("Error fetching weather data");
                    }
                    searchButton.setEnabled(true);
                    locationButton.setEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    weatherArea.setText("Error: " + ex.getMessage());
                    statusLabel.setText("Error occurred");
                    searchButton.setEnabled(true);
                    locationButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void fetchWeatherByLocation() {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Detecting your location...");
                    searchButton.setEnabled(false);
                    locationButton.setEnabled(false);
                });

                LocationService.LocationData location = LocationService.getCurrentLocation();
                WeatherData weatherData = WeatherService.getWeatherData(
                    location.getLat(), location.getLon());
                
                SwingUtilities.invokeLater(() -> {
                    if (weatherData != null) {
                        displayWeatherData(weatherData);
                        cityField.setText(location.getCity());
                        statusLabel.setText("Location-based weather loaded successfully");
                    } else {
                        weatherArea.setText("Error: Could not fetch weather data for your location");
                        statusLabel.setText("Error fetching weather data");
                    }
                    searchButton.setEnabled(true);
                    locationButton.setEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    weatherArea.setText("Error: " + ex.getMessage());
                    statusLabel.setText("Error occurred");
                    searchButton.setEnabled(true);
                    locationButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void displayWeatherData(WeatherData weatherData) {
        String weatherInfo = String.format("""
            ==================================
                  WEATHER FORECAST
            ==================================
            
            Location: %s, %s
            Coordinates: %.4f, %.4f
            
            Current Conditions:
            • Temperature: %.1f°C (%.1f°F)
            • Feels like: %.1f°C
            • Description: %s
            • Humidity: %d%%
            • Pressure: %.1f hPa
            • Wind Speed: %.1f m/s
            
            Last Updated: %s
            ==================================
            """,
            weatherData.getCityName(),
            weatherData.getCountry(),
            weatherData.getLatitude(),
            weatherData.getLongitude(),
            weatherData.getTemperatureCelsius(),
            weatherData.getTemperatureFahrenheit(),
            weatherData.getFeelsLike() - 273.15,
            weatherData.getDescription(),
            weatherData.getHumidity(),
            weatherData.getPressure(),
            weatherData.getWindSpeed(),
            weatherData.getDateTime()
        );

        weatherArea.setText(weatherInfo);
    }
}