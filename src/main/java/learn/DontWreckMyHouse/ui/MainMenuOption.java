package learn.DontWreckMyHouse.ui;

public enum MainMenuOption {

    EXIT(0, "Exit", false),
    MAKE_A_RESERVATION(1, "Make a Reservation", false),
    VIEW_RESERVATION(2, "View Reservations for Host", false),
    EDIT_A_RESERVATION(3, "Edit a Reservation", false),
    CANCEL_A_RESERVATION(4, "Cancel a Reservation", false);


    private int value;
    private String message;
    private boolean hidden;

    MainMenuOption(int value, String message, boolean hidden) {
        this.value = value;
        this.message = message;
        this.hidden = hidden;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHidden() {
        return hidden;
    }
}


