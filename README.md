# MagikMarket

Developed by Enri Sulejmani, Luca Vannuccini, and Alessandro Sellani.

## About

MagikMarket is an educational API-based application designed to provide users with a realistic and risk-free environment to learn about investing in stocks and gain valuable insights into the dynamics of the stock market. This project allows users to simulate stock trading without using real money, making it an ideal tool for beginners, students, or anyone interested in understanding how the stock market works.

Link to the tutorial video: https://youtu.be/9qTNd8eBl7I

## Features

- **User Registration and Login:** Users can securely register and log in to the application using encrypted passwords.

- **Stock Listing:** A comprehensive listing of available stocks is provided, enabling users to explore various investment opportunities.

- **Stock Prediction:** The application offers predictive analysis to assist users in making informed investment decisions. Predictions are randomized but predicting algorithms can be implemented.

- **Interactive Charts:** Visual representations such as line charts and pie charts are incorporated to help users analyze stock performance and market trends easily. Users have the flexibility to select a maximum of four stocks for visualization.

## Technologies Used

MagikMarket utilizes modern technologies to deliver a seamless user experience:

- **Frontend:** JavaFX is employed for the frontend development, providing a rich graphical interface.

- **Backend:** Java 17 powers the backend logic, ensuring robustness and reliability.

- **Database Management:** CSV files are utilized for database management, offering simplicity and flexibility.

### API Integration

The application integrates with the Yahoo Finance API, allowing access to real-time stock data for up to 20,000 calls per month. This integration enhances the application's functionality and provides users with accurate market information.

## Setup and Installation

To set up and run MagikMarket on your local machine, follow these steps:

1. Clone the repository to your local machine using the following command:
   **git clone git@github.com:MrVannu/MagikMarket.git**

2. Navigate to the project directory in your command-line interface.

3. Install the necessary dependencies by running the following Maven command:
    **mvn clean install**

4. Launch the application by executing the following Maven command:
   **mvn exec:java**

5. Ensure that your machine is connected to the internet to access real-time market data.

6. Congratulations! You are now ready to explore MagikMarket and start learning about stock investing in a risk-free environment.

## Usage

MagikMarket provides a user-friendly interface with the following features:

- **User Authentication:** Users can register for an account and log in securely to access the application's features.

- **Customization:** Users have the option to customize their profiles, including their initial investment amount and profile image.

- **Stock Management:** Users can browse and select stocks from the available listing, view detailed information, and make transactions.

- **Prediction Tools:** The application offers predictive analysis tools to assist users in making investment decisions based on historical data and market trends.

- **Transaction History:** Users can view their transaction history to track their investment activities and monitor performance over time.

## Detailed overview and instructions

Below is a comprehensive overview of the application, detailing its functionalities:

User Authentication:
The initial interaction involves user login or registration. During registration, users are required to input a username 
(maximum 15 characters), a password (maximum 15 characters), and a valid email. The email must adhere to the standard email
format (example@host.com). Upon successful registration, users can proceed to log in, and a successful login directs them to the application's home page.

Data Visualization:
To begin utilizing the app, users can select one or more checkboxes (up to a maximum of 4) to populate charts and view information
related to the selected stocks. Clicking the "Prevision" button generates a new window displaying a forecast for the selected checkboxes. 
These features primarily serve educational purposes.

Interactive Transactions:
In the interactive segment of the project, users can buy or sell specific stocks, always considering their available funds. 
Upon executing transactions and pressing the "Switch Pane" button, relevant data is presented. The initial view displays a history 
of all user transactions, while the second view offers additional insights, including the current market price of each stock, the 
quantity owned by the user, and the average gain for each stock. Green values indicate an uptrend in the value of a specific stock. 
Grey values signify a stable market price with no significant changes, while red values denote a decrease in the stock's price.

Menu Bar and Settings:
At the top of the application, users find a menu bar providing instructions on application usage. Additionally, users can modify 
their available funds and log out of the current session.

User Customization:
Users have the option to personalize their account by loading an image. This feature is accessible by clicking the button next 
to the username displayed at the application's top.


## License

MagikMarket is developed for educational purposes. You are free to use, modify, and distribute the application according to the terms of the license.

## Contributors

We would like to thank the following contributor for making different icons and especially the logo of the application:

- Diletta Maria Dell'Utri

