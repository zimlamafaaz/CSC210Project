/*
    Fathima and Oscar
    CSC 210 0900
 */
package com.example.javaproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class IceCreamStores extends Application {

    // Instance of IceCreamStore for managing item details and calculations
    IceCreamStore store = new IceCreamStore();

    @Override
    public void start(Stage stage) {
        // Initialize application by setting up the login pane
        setupStage(stage, createLoginPane(stage));
    }

    // Creates the login pane layout
    private GridPane createLoginPane(Stage stage) {
        GridPane loginPane = new GridPane();
        loginPane.setHgap(10);
        loginPane.setVgap(10);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setStyle("-fx-background-color: #f7f3e9; -fx-padding: 20px;");

        // Username label and input field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-padding: 5px;");

        // Password label and input field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-padding: 5px;");

        // Login button and action handler for authentication
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Validate credentials and navigate to menu pane if successful
            if (username.equals(store.getUsername()) && password.equals(store.getPassword())) {
                setupStage(stage, createMenuPane(stage));
            } else {
                // Show error alert on invalid credentials
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials. Please try again.");
                alert.showAndWait();
            }
        });

        // Adding components to the grid
        loginPane.add(usernameLabel, 0, 0);
        loginPane.add(usernameField, 1, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(loginButton, 1, 2);

        return loginPane;
    }

    // Creates the menu pane where items can be selected
    private GridPane createMenuPane(Stage stage) {
        GridPane menuPane = new GridPane();
        menuPane.setHgap(20);
        menuPane.setVgap(20);
        menuPane.setAlignment(Pos.CENTER);
        menuPane.setPadding(new Insets(20));
        menuPane.setStyle("-fx-background-color: #f7f3e9;");

        // Fetch items and initialize quantity array
        String[] items = store.getItemName();
        int[] quantities = new int[items.length];

        // Grid for displaying items
        GridPane productPane = new GridPane();
        productPane.setHgap(20);
        productPane.setVgap(20);
        productPane.setAlignment(Pos.CENTER);

        // Loop through items to create individual item panes
        for (int i = 0; i < items.length; i++) {
            final int index = i;
            String itemName = items[index];
            double price = store.getPrice(itemName);

            GridPane itemPane = new GridPane();
            itemPane.setHgap(10);
            itemPane.setVgap(5);
            itemPane.setAlignment(Pos.CENTER_LEFT);

            // Item details and controls
            Label nameLabel = new Label(itemName);
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Label priceLabel = new Label(String.format("$%.2f", price));
            priceLabel.setStyle("-fx-text-fill: #00796b; -fx-font-size: 12px;");
            Label quantityLabel = new Label("Qty: 0");
            quantityLabel.setStyle("-fx-text-fill: #f57f17; -fx-font-size: 12px;");

            Button incrementButton = new Button("+");
            Button decrementButton = new Button("-");

            // Button styles and actions
            incrementButton.setPrefWidth(30);
            incrementButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            decrementButton.setPrefWidth(30);
            decrementButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

            incrementButton.setOnAction(e -> {
                quantities[index]++;
                quantityLabel.setText("Qty: " + quantities[index]);
            });

            decrementButton.setOnAction(e -> {
                if (quantities[index] > 0) {
                    quantities[index]--;
                    quantityLabel.setText("Qty: " + quantities[index]);
                }
            });

            // Add components to the item pane
            itemPane.add(nameLabel, 0, 0);
            itemPane.add(priceLabel, 1, 0);
            itemPane.add(decrementButton, 2, 0);
            itemPane.add(quantityLabel, 3, 0);
            itemPane.add(incrementButton, 4, 0);

            // Add item pane to the product grid
            productPane.add(itemPane, i % 2, i / 2);
        }

        // Add the product grid to the menu pane
        menuPane.add(productPane, 0, 0);

        // Back and checkout buttons with actions
        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px;");
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #009688; -fx-text-fill: white; -fx-font-size: 14px;");

        backButton.setOnAction(e -> setupStage(stage, createLoginPane(stage)));
        checkoutButton.setOnAction(e -> {
            // Check if any item has a quantity greater than 0
            boolean hasSelection = false;
            for (int quantity : quantities) {
                if (quantity > 0) {
                    hasSelection = true;
                    break;
                }
            }

            // If no item was selected, show an alert
            if (!hasSelection) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select at least one item before proceeding to checkout.");
                alert.showAndWait();
            } else {
                // Proceed to the checkout page if an item was selected
                setupStage(stage, createCheckoutPane(stage, items, quantities));
            }
        });

        // Add buttons to a separate grid for layout
        GridPane buttonPane = new GridPane();
        buttonPane.setHgap(10);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.add(backButton, 0, 0);
        buttonPane.add(checkoutButton, 1, 0);

        menuPane.add(buttonPane, 0, 1);
        return menuPane;
    }

    private GridPane createCheckoutPane(Stage stage, String[] items, int[] quantities) {
        // Create a grid layout for the checkout page
        GridPane checkoutPane = new GridPane();
        checkoutPane.setHgap(10); // Set horizontal gap between elements
        checkoutPane.setVgap(10); // Set vertical gap between elements
        checkoutPane.setAlignment(Pos.CENTER); // Align the grid to the center of the window
        checkoutPane.setPadding(new Insets(20)); // Add padding around the grid
        checkoutPane.setStyle("-fx-background-color: #f7f3e9;"); // Set background color

        // Add a header label for the checkout page
        Label checkoutLabel = new Label("Checkout");
        checkoutLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;"); // Apply styles for the header
        checkoutPane.add(checkoutLabel, 0, 0, 2, 1); // Place the header in the first row, spanning two columns

        // Create a nested grid for displaying the purchased items
        GridPane cartPane = new GridPane();
        cartPane.setHgap(10); // Set horizontal gap for item details
        cartPane.setVgap(10); // Set vertical gap for item rows
        checkoutPane.add(cartPane, 0, 1, 2, 1); // Add the cart pane to the checkout layout

        double totalCost = 0; // Initialize the total cost
        int row = 0; // Row counter for dynamically adding items to the grid

        // Iterate through all items and display only those with a quantity greater than 0
        for (int i = 0; i < items.length; i++) {
            if (quantities[i] > 0) { // Check if the item was purchased
                String itemName = items[i]; // Get the name of the item
                int quantity = quantities[i]; // Get the quantity of the item
                double itemCost = store.calcCost(itemName, quantity); // Calculate the cost for the item

                // Create labels for item name, quantity, and cost
                Label nameLabel = new Label(itemName);
                nameLabel.setStyle("-fx-font-size: 14px;"); // Style for item name
                Label quantityLabel = new Label(String.valueOf(quantity));
                quantityLabel.setStyle("-fx-font-size: 14px;"); // Style for quantity
                Label costLabel = new Label(String.format("$%.2f", itemCost));
                costLabel.setStyle("-fx-text-fill: #00796b; -fx-font-size: 14px;"); // Style for cost

                // Add these labels to the cart grid
                cartPane.add(nameLabel, 0, row); // Add item name to the first column
                cartPane.add(quantityLabel, 1, row); // Add quantity to the second column
                cartPane.add(costLabel, 2, row); // Add cost to the third column

                totalCost += itemCost; // Update the total cost
                row++; // Move to the next row for the next item
            }
        }

        // Calculate taxes and total cost
        double tax = store.calcTaxes(totalCost); // Calculate tax (e.g., 8% of the total)
        double finalCost = store.calcTotalCost(totalCost); // Calculate the final cost including tax

        // Create labels for displaying subtotal, tax, and total
        Label subtotalLabel = new Label(String.format("Subtotal: $%.2f", totalCost));
        Label taxLabel = new Label(String.format("Tax (8%%): $%.2f", tax));
        Label totalLabel = new Label(String.format("Total: $%.2f", finalCost));

        // Apply styles to the labels
        subtotalLabel.setStyle("-fx-font-size: 14px;");
        taxLabel.setStyle("-fx-font-size: 14px;");
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Add these labels to the checkout grid below the item list
        checkoutPane.add(subtotalLabel, 0, row + 1); // Subtotal row
        checkoutPane.add(taxLabel, 0, row + 2); // Tax row
        checkoutPane.add(totalLabel, 0, row + 3); // Total row

        // Create a button to return to the menu
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;"); // Style for the button
        backButton.setOnAction(e -> setupStage(stage, createMenuPane(stage))); // Action to navigate back to the menu

        // Create a button to confirm the purchase
        Button checkoutButton = new Button("Confirm");
        checkoutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Style for the button
        checkoutButton.setOnAction(e -> {
            // Show confirmation alert on purchase
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thank you for your purchase!");
            alert.showAndWait();
            // Navigate back to the menu after confirmation
            setupStage(stage, createMenuPane(stage));
        });

        // Add the buttons to the checkout grid
        checkoutPane.add(backButton, 0, row + 4); // Add the back button below total
        checkoutPane.add(checkoutButton, 1, row + 4); // Add the confirm button next to the back button

        return checkoutPane; // Return the completed checkout pane layout
    }


    private void setupStage(Stage stage, GridPane pane) {
        Scene scene = new Scene(pane, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Creamology");
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
