package com.example.demo1;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class HelloController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private AnchorPane scenePane;

    private AnchorPaneCust curTabContainer;
    private Tab curTab;
    private WebView curWebView;
    private WebEngine curEngine;
    private TextField curSearchBar;
    private WebHistory curHistory;
    private ProgressBar curProgressBar;
    private double zoomRatio = 1;
    private String homePage_url;
    private String notfoundPage_url;
    private Stage stage;
    private String text;
    private String tabId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homePage_url = getClass().getResource("pages/home.html").toExternalForm();
        notfoundPage_url = getClass().getResource("pages/notfound.html").toExternalForm();

        // reformat urls
        if (homePage_url.matches("^file:/[a-zA-Z0-9].*")) {
            homePage_url = homePage_url.replace("file:", "file://");
        }

        if (notfoundPage_url.matches("^file:/[a-zA-Z0-9].*")) {
            notfoundPage_url = notfoundPage_url.replace("file:", "file://");
        }

        removeAllTab();
        addNewTab();
    }

    public void removeAllTab() {
        int t = tabPane.getTabs().size();

        for (int i=0; i < t - 1; i++) {
            tabPane.getTabs().remove(0);
        }
    }

    // View
    public AnchorPaneCust initNewTabContent() {
        /* Initialization */
        AnchorPaneCust pane = new AnchorPaneCust();
        Button searchButton = new Button("Search");
        Button newTabButton = new Button("+");
        Button zoomOutButton = new Button("Z-");
        Button zoomInButton = new Button("Z+");
        TextField searchBar = new TextField();
        WebView webView = new WebView();
        ProgressBar progressBar = new ProgressBar();

        Image clockwise = new Image(getClass().getResource("assets/icons/arrow-clockwise.png").toExternalForm());
        Image leftSquare = new Image(getClass().getResource("assets/icons/arrow-left-square.png").toExternalForm());
        Image rightSquare = new Image(getClass().getResource("assets/icons/arrow-right-square.png").toExternalForm());

        ImageView nextButtonImage = new ImageView(rightSquare);
        ImageView previousButtonImage = new ImageView(leftSquare);
        ImageView reloadButtonImage = new ImageView(clockwise);

        /************************/

        // searchBar configuration
        searchBar.setLayoutX(163);
        searchBar.setLayoutY(5);
        searchBar.setMinWidth(300);
        searchBar.setPromptText("Search...");
        searchBar.setOnKeyPressed((KeyEvent event) -> makeSearchKey(event));

        // searchButton configuration
        searchButton.setLayoutX(1097);
        searchButton.setLayoutY(5);
        searchButton.setMnemonicParsing(false);
        searchButton.setPrefHeight(26);
        searchButton.setPrefWidth(100);
        searchButton.getStyleClass().add("btn");
        searchButton.setOnAction(event -> makeSearch());

        /**** Controls buttons & controls images ***/
        // progress bar
        progressBar.setLayoutY(30);
        progressBar.setPrefHeight(10);
        progressBar.setVisible(false);
        // previous button image
        previousButtonImage.setLayoutY(5);
        previousButtonImage.setLayoutX(4);
        previousButtonImage.setOnMouseClicked(mouseEvent -> goToPreviews());
        // next button image
        nextButtonImage.setLayoutX(37);
        nextButtonImage.setLayoutY(5);
        nextButtonImage.setOnMouseClicked(mouseEvent -> goToNext());
        // reload button image
        reloadButtonImage.setLayoutX(68);
        reloadButtonImage.setLayoutY(5);
        reloadButtonImage.setOnMouseClicked(mouseEvent -> reloadPage());
        // new tab button
        newTabButton.setLayoutX(97);
        newTabButton.setLayoutY(5);
        newTabButton.setMnemonicParsing(false);
        newTabButton.setOnAction(event -> addNewTab());
        /**************************************/

         /*
        // zoom out configuration
        zoomOutButton.setLayoutY(5);
        zoomOutButton.setLayoutX(20);
        zoomOutButton.setMnemonicParsing(false);
        zoomOutButton.setOnAction(event -> zoomPageOut());

        // zoom in configuration
        zoomInButton.setLayoutY(5);
        zoomInButton.setLayoutX(40);
        zoomInButton.setMnemonicParsing(false);
        zoomInButton.setOnAction(event -> zoomPageIn());*/


        /**** Manage responsive ****/
        // for webView
        setResponsive(webView, 0, 0, 37, 0);
        // for searchBar
        setResponsive(searchBar, "r", 305);
        setResponsive(searchBar, "l", 223);
        setResponsive(searchBar, "t", 5);
        // for searchButton
        setResponsive(searchButton, "r", 190);
        setResponsive(searchButton, "t", 5);
        // for progressBar
        setResponsive(progressBar, "r", 0);
        setResponsive(progressBar, "l", 0);
        /***************************/


        // add nodes to pane (AnchorPaneCust)
        pane.addChild(previousButtonImage, "previousButtonImage");
        pane.addChild(nextButtonImage, "nextButtonImage");
        pane.addChild(reloadButtonImage, "reloadButtonImage");
        pane.addChild(newTabButton, "newTabButton");
        pane.addChild(progressBar, "progressBar");
        pane.addChild(searchButton, "searchButton");
        pane.addChild(searchBar, "searchBar");
        pane.addChild(webView, "webView");
//        pane.addChild(zoomInButton, "zoomInButton");
//        pane.addChild(zoomOutButton, "zoomOutButton");

        return pane;
    }
    public void setResponsive(Node node, double right, double left, double top, double bottom) {
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bottom);
    }
    public void setResponsive(Node node, String side, double size) {
        switch (side.toUpperCase()) {
            case "R":
                AnchorPane.setRightAnchor(node, size);
                break;
            case "L":
                AnchorPane.setLeftAnchor(node, size);
                break;
            case "T":
                AnchorPane.setTopAnchor(node, size);
                break;
            case "D":
                AnchorPane.setBottomAnchor(node, size);
        }
    }

    public Tab newTab() {
        // Note: tab and his Close button have same id

        Tab tab = new Tab();
        tabId = UUID.randomUUID().toString();
        Button closeButton = new Button("x");
        AnchorPaneCust pane = initNewTabContent();

        // tab close button
        closeButton.getStyleClass().add("btn");
        closeButton.getStyleClass().add("btn-border-none");
        closeButton.setStyle("-fx-font-weight: bold");
        closeButton.setMnemonicParsing(false);
        closeButton.setId(tabId);
        closeButton.setOnAction(event -> closeCurrentTab(event));

        // configure tab
        tab.setText("New blank");
        tab.setId(tabId);
        tab.setContent(pane);
        tab.setOnSelectionChanged(event -> {
            updateStateData();
        });
        tab.setGraphic(closeButton);

        return tab;
    }

    public void addNewTab() {
        int nbTabs = tabPane.getTabs().size();

        curTab = newTab();;
        curTabContainer = (AnchorPaneCust) curTab.getContent();
        curWebView = (WebView) curTabContainer.getChild("webView");
        curSearchBar = (TextField)  curTabContainer.getChild("searchBar");
        curEngine = curWebView.getEngine();
        curHistory = curEngine.getHistory();
        curProgressBar = (ProgressBar) curTabContainer.getChild("progressBar");

        // listener on Engine
        curEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> engineListener(obs, oldValue, newValue));

        curEngine.setOnVisibilityChanged(event -> {
            System.out.println("dssds");
        });

        // progress bar binding with engine load progress
        curProgressBar.progressProperty().bind(curEngine.getLoadWorker().progressProperty());

        // push new tab
        tabPane.getTabs().add(nbTabs, curTab);

//        changeCurrentTab(tabPane.getTabs().get(nbTabs - 1));
        loadPage(homePage_url);

        // update state data
        updateStateData();
    }

    public void engineListener(ObservableValue<? extends Worker.State> obs, Worker.State oldValue, Worker.State newValue) {
        switch (newValue) {
            case RUNNING:
                // loading process is running
                // progress set visible
                curProgressBar.setVisible(true);

                text = curEngine.getLocation();

                // update search bar content
                if (!text.equals(homePage_url) && !text.equals(notfoundPage_url)) {
                    curSearchBar.setText(text);
                }

                break;
            case SUCCEEDED:
                // loading process is terminated with success
                String title = curEngine.getTitle();
                updateTitleOnView(title);

                // progress set hidden
                curProgressBar.setVisible(false);
                break;
            case FAILED:
                // loading process fail
                // load not found page
                loadPage(notfoundPage_url);
                break;
        }
    }
    ///////////

    // getters
    public Tab getCurrentTab()  {
        SingleSelectionModel<Tab> t = tabPane.getSelectionModel();

        return t.getSelectedItem();
    }

    public WebView getCurrentWebView() {
        Tab tab = getCurrentTab();

        WebView webView = (WebView) ((AnchorPaneCust) tab.getContent()).getChild("webView");

        return webView;
    }

    public AnchorPaneCust getCurrentTabContainer() {
        return (AnchorPaneCust) getCurrentTab().getContent();
    }

    public ProgressBar getCurrentProgressBar() {
        AnchorPaneCust tab_container = (AnchorPaneCust) getCurrentTab().getContent();
        ProgressBar progress = (ProgressBar) tab_container.getChild("progressBar");

        return progress;
    }

    public TextField getCurrentSearchBar() {
        AnchorPaneCust tab_container = (AnchorPaneCust) getCurrentTab().getContent();
        TextField textField = (TextField) tab_container.getChild("searchBar");

        return textField;
    }

    public WebEngine getCurrentEngine() {
        WebView webView = getCurrentWebView();

        return webView.getEngine();
    }

    public WebHistory getCurrentHistory() {

        WebHistory history = getCurrentEngine().getHistory();

        return history;
    }
    /////////

    public void changeCurrentTab(Tab tab) {
       SingleSelectionModel<Tab> t = new SingleSelectionModel<Tab>() {
           @Override
           protected Tab getModelItem(int i) {
               return null;
           }

           @Override
           protected int getItemCount() {
               return 0;
           }
       };
       t.select(tab);
       tabPane.setSelectionModel(t);
    }

    public void updateStateData() {
        curTab = getCurrentTab();
        curSearchBar = getCurrentSearchBar();
        curEngine = getCurrentEngine();
        curWebView = getCurrentWebView();
        curHistory = getCurrentHistory();
        curProgressBar = getCurrentProgressBar();
        curTabContainer = getCurrentTabContainer();
    }

    public void updateStageTitle(String title) {
        /* Update stage title
         *
         * */

        stage = (Stage) scenePane.getScene().getWindow();

        if (title != null && !title.isEmpty()) {
            // update stage title
            if (title.length() > 50)  {
                stage.setTitle("Solina Browser | " + title.substring(0, 50) + "...");
            }else {
                stage.setTitle("Solina Browser | " + title);
            }
        }
    }

    public void updateTabTitle(String title) {
        /* Update current tab title
         *
         * */

        stage = (Stage) scenePane.getScene().getWindow();

        if (title != null && !title.isEmpty()) {
            // tab title
            if (title.length() > 20) {
                curTab.setText(title.substring(0, 20) + "...");
            }else {
                curTab.setText(title);
            }
        }
    }

    public void updateTitleOnView(String title) {
        /* Update stage title and current tab title
        *
        * */

       updateStageTitle(title);
       updateTabTitle(title);
    }

    public void loadPage(String url) {
        curEngine.load(url);
    }

    public void reloadPage() {
        curEngine.reload();
    }

    public void goToPreviews() {
        ObservableList<WebHistory.Entry> entries = curHistory.getEntries();
        String text;
        String title;

        try {
            curHistory.go(-1);

            text = entries.get(curHistory.getCurrentIndex()).getUrl();
            curSearchBar.setText("");

            if (!text.equals(homePage_url) && !text.equals(notfoundPage_url)) {
                curSearchBar.setText(text);
            }

            title = entries.get(curHistory.getCurrentIndex()).getTitle();

            // update stage title and tab title
            updateTitleOnView(title);
        }catch (Exception e) {
            System.out.println("Go back error: " + e.getMessage());
        }
    }

    public void goToNext() {
        ObservableList<WebHistory.Entry> entries = curHistory.getEntries();
        String text;
        String title;

        try {
            curHistory.go(1);

            text = entries.get(curHistory.getCurrentIndex()).getUrl();
            curSearchBar.setText("");

            if (!text.equals(homePage_url) && !text.equals(notfoundPage_url)) {
                curSearchBar.setText(text);
            }

            title = entries.get(curHistory.getCurrentIndex()).getTitle();

            // update stage title and tab title
            updateTitleOnView(title);
        }catch (Exception e) {
            System.out.println("Go forward error: " + e.getMessage());
        }
    }

    public void closeCurrentTab(ActionEvent event) {
        Button targetButton = (Button) event.getTarget();
        String tabId = targetButton.getId();
        Tab targetTab = null;

        // find target tab
        // Note: tab and his close button have same id
        for (Tab tab: tabPane.getTabs()) {
            if (tab.getId().equals(tabId)) {
                targetTab = tab;
                break;
            }
        }

        tabPane.getTabs().remove(targetTab);

        // add new tab if tan pane is empty
        if (tabPane.getTabs().size() == 0)
            addNewTab();

        // update state data:
        updateStateData();
    }

    public void zoomPageOut() {
        zoomRatio -= 0.25;
        if (zoomRatio < 0) zoomRatio = 0;

        curWebView.setZoom(zoomRatio);
    }

    public void zoomPageIn() {
        zoomRatio += 0.25;
        if (zoomRatio < 0) zoomRatio = 0;

        curWebView.setZoom(zoomRatio);
    }

    public void makeSearch() {
        String searchEngineUrl = SearchEngine.engines.get("google").getUrl();
        String text = curSearchBar.getText();

        if (!text.isEmpty()) {

            // update search text format
            if (!text.matches("^[a-zA-Z]+://.*")) {
                if (text.matches("^http:/[a-zA-Z0-9].*")) {
                    text = "http://" + text.substring(6, text.length());
                }else if (text.matches("^https:/[a-zA-Z0-9].*")) {
                    text = "https://" + text.substring(7, text.length());
                }else if (text.matches("^[a-zA-Z]+:/.*")) {

                }else if (text.matches("^.*[a-zA-Z0-9]\\.[a-zA-Z].*")){
                    text = "https://" + text;
                }else {
                    text = searchEngineUrl + text;
                }
            }

            // update search bar text
            curSearchBar.setText(text);

            loadPage(text);
        }
    }

    public void makeSearch(ActionEvent e) {
        makeSearch();
    }

    public void makeSearchKey(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                makeSearch();
                break;
        }
    }

}