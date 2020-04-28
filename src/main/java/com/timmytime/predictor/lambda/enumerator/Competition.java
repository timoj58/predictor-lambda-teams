package com.timmytime.predictor.lambda.enumerator;

public enum Competition {

    england_1("Premier League", "English"),
    england_2("Championship", "English"),
    england_3("League One", "English"),
    england_4("League Two", "English"),
    england_5("National League", "English"),
    italy_1("Serie A", "Italian"),
    italy_2("Serie B", "Italian"),
    spain_1("La Liga", "Spanish"),
    spain_2("La Liga 2", "Spanish"),
    france_1("Ligue 1", "French"),
    france_2("Ligue 2", "French"),
    german_1("Bundesliga", "German"),
    german_2("2. Bundesliga", "German"),
    scotland_1("Premiership", "Scottish"),
    scotland_2("Championship", "Scottish"),
    scotland_3("League One", "Scottish"),
    scotland_4("League Two", "Scottish"),
    greece_1("Superleague", "Greek"),
    turkey_1("Süper Lig", "Turkish"),
    norway_1("Eliteserien", "Norwegian"),
    russia_1("Чемпионат России по футболу", "Russian"),
    sweden_1("Allsvenskan", "Swedish"),
    denmark_1("1st Division", "Danish"),
    belgium_1("First Division A", "Belgium"),
    holland_1("Eredivisie", "Dutch"),
    portugal_1("Primeira Liga", "Portuguese");

    private String label;
    private String country;

    Competition(String label, String country) {
        this.label = label;
        this.country = country;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
