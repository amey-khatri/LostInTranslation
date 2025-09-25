package translation;

import javax.swing.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    private static void updateTranslation(JList<String> list, JComboBox<String> languageComboBox,
                                          CountryCodeConverter countryCodeConverter,
                                          LanguageCodeConverter languageCodeConverter,
                                          Translator translator, JLabel resultLabel) {
        String country = list.getSelectedValue();
        String language = (String) languageComboBox.getSelectedItem();

        if (country != null && language != null) {
            String countryCode = countryCodeConverter.fromCountry(country).toLowerCase();
            String languageCode = languageCodeConverter.fromLanguage(language);
            String translation = translator.translate(countryCode, languageCode);
            resultLabel.setText(translation);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

            JPanel countryPanel = new JPanel();
            String[] items = new String[translator.getCountryCodes().size()];

            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryCodeConverter.fromCountryCode(countryCode);
            }

            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);


            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);

            JPanel translationPanel = new JPanel();
            translationPanel.add(new JLabel("Translation:"));
            JLabel resultLabel = new JLabel(" ");
            translationPanel.add(resultLabel);


            // JList listener
            list.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslation(list, languageComboBox, countryCodeConverter, languageCodeConverter, translator, resultLabel);
                }
            });

            // JComboBox listener
            languageComboBox.addActionListener(e -> updateTranslation(list, languageComboBox, countryCodeConverter, languageCodeConverter, translator, resultLabel));



            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
