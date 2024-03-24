package com.example.theshoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText customerNameText;
    AutoCompleteTextView provinceText;
    RadioGroup computerTypeRadioGroup, laptopPeripherals, desktopPeripherals;
    Spinner spinner;
    CheckBox ssdCheckBox, printerCheckBox;
    TextView peripheralsTextView, invoiceTitle, invoiceText;

    //list of provinces (for checking valid input of province)
    final private List<String> provinces = Arrays.asList(
            "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
            "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan"
    );

    //list of brands for displaying in the spinner (drop-down list)
    private ArrayList<String> brands;
    private final Invoice invoice = new Invoice(); //creating object of Invoice class
    private double baseCost = 0; //cost without tax
    final double TAX_RATE = 0.13; // constant for 13% TAX


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //customer name
        customerNameText = findViewById(R.id.customerNameText);

        //province
        provinceText = findViewById(R.id.provinceText);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, provinces);
        provinceText.setAdapter(provinceAdapter);

        //computer type and added peripherals
        peripheralsTextView = findViewById(R.id.peripheralsTextView);
        computerTypeRadioGroup = findViewById(R.id.computerTypeRadioGroup);
        laptopPeripherals = findViewById(R.id.laptopPeripherals);
        desktopPeripherals = findViewById(R.id.desktopPeripherals);

        //to handle the radio button click for computer type radio group (laptop and desktop)
        computerTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //if radio button for desktop is clicked
                if (checkedId == R.id.desktopRadioButton) {
                    peripheralsTextView.setVisibility(View.VISIBLE);
                    desktopPeripherals.setVisibility(View.VISIBLE);
                    laptopPeripherals.setVisibility(View.GONE);
                }
                //if radio button for laptop is clicked
                else if (checkedId == R.id.laptopRadioButton) {
                    peripheralsTextView.setVisibility(View.VISIBLE);
                    laptopPeripherals.setVisibility(View.VISIBLE);
                    desktopPeripherals.setVisibility(View.GONE);
                }
            }
        });

        //computer brand
        spinner = findViewById(R.id.brandSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String brand = parent.getItemAtPosition(position).toString();
                invoice.setBrand(brand);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //arraylist for viewing brands in the spinner
        brands = new ArrayList<>();
        brands.add("Select a brand");
        brands.add("Dell");
        brands.add("HP");
        brands.add("Lenova");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        //Calculate Button
        Button calculateInvoiceBtn = findViewById(R.id.calculateButton);
        calculateInvoiceBtn.setOnClickListener(new View.OnClickListener() { // when the button is clicked
            @Override
            public void onClick(View view) {
                //invoice
                invoiceTitle = findViewById(R.id.invoiceTitle);
                invoiceText = findViewById(R.id.invoiceText);
                invoiceTitle.setText(""); //clears invoice label
                invoiceText.setText(""); //clears the invoice

                //validation for customer name and province
                boolean isEnteredValueValid = validateEnteredValue(view);
                //if there is any invalidation in 'validateEnteredValue()' method, button click method is ended
                if(!isEnteredValueValid){
                    return;
                }


                //validation for computer type, peripherals and brands
                boolean isSelectionValid = validateComputerDetails(view);
                //if there is any invalidation in 'validateComputerDetails()' method, button click method is ended
                if(!isSelectionValid){
                    return;
                }

                //total cost from the additional devices
                double additionalCost = validateAdditional(); //validation for the additional
                baseCost = baseCost + additionalCost;

                //calculate total cost including TAX
                double totalCost = baseCost * ( 1 + TAX_RATE);
                invoice.setTotalCost(String.format(Locale.ENGLISH,"$%.2f", totalCost));

                //text to be shown when the Calculate Button is clicked
                String result = "Customer: " + invoice.getCustomerName() + "\n" +
                        "Province: " + invoice.getProvince() + "\n" +
                        "Computer: " + invoice.getComputerType() + "\n" +
                        "Brand: " + invoice.getBrand() + "\n" +
                        "Additional: " + invoice.getAdditional() + "\n" +
                        "Added Peripherals: " + invoice.getPeripherals() + "\n" +
                        "Cost: " + invoice.getTotalCost();
                invoiceTitle.setText(R.string.invoice_label);
                invoiceText.setText(result);

            }
        });

    }

    //validation for customer name and province
    private boolean validateEnteredValue(View view){
        //validation of the entered customer name
        String customerName = customerNameText.getText().toString().trim();
        if(customerName.isEmpty()){
            Toast.makeText(view.getContext(), "Customer Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        invoice.setCustomerName(customerName);

        //validations of the entered province name
        String province = provinceText.getText().toString().trim();
        if(province.isEmpty()){
            Toast.makeText(view.getContext(), "Province Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(provinces.contains(province))) {
            Toast.makeText(view.getContext(), "Invalid Province Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        invoice.setProvince(province);
        return true;
    }

    //validation for computer type, peripherals and brands
    private boolean validateComputerDetails(View view){

        //validation for the radio group of computer type
        int selectedRadioButtonId = computerTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) { // Check if any radio button is selected
            RadioButton selectedComputerType = findViewById(selectedRadioButtonId);
            String computerType = selectedComputerType.getText().toString();
            invoice.setComputerType(computerType);


            //validate brand
            //checks if the user has selected a brand or not
            if(Objects.equals(invoice.getBrand(), brands.get(0))){
                Toast.makeText(view.getContext(), "Please select a brand", Toast.LENGTH_SHORT).show();
                return false;
            }

            //selection of the cost of the laptop computers based on brand
            if(computerType.equals("Laptop")){
                String brand = invoice.getBrand();
                switch (brand) {
                    case "Dell":
                        baseCost = 1249;
                        break;
                    case "Lenova":
                        baseCost = 1549;
                        break;
                    case "HP":
                        baseCost = 1150;
                        break;
                }

                //validation for the radio group of peripherals
                int selectedLaptopPeripheralsId = laptopPeripherals.getCheckedRadioButtonId();
                if (selectedLaptopPeripheralsId != -1){
                    RadioButton selectedLaptopPeripheral = findViewById(selectedLaptopPeripheralsId);
                    String peripherals = selectedLaptopPeripheral.getText().toString();
                    invoice.setPeripherals(peripherals);

                    if(selectedLaptopPeripheralsId == R.id.coolingMatRadioButton) {
                        baseCost += 33;
                    }
                    else if(selectedLaptopPeripheralsId == R.id.usbHubRadioButton) {
                        baseCost += 60;
                    }
                    else if(selectedLaptopPeripheralsId == R.id.laptopStandRadioButton) {
                        baseCost += 139;
                    }


                }
                else{
                    Toast.makeText(view.getContext(), "Please select one peripheral", Toast.LENGTH_SHORT).show();
                    return false;
            }
            }

            //selection of the cost of the desktop computers based on brand
            if(computerType.equals("Desktop")){
                String brand = invoice.getBrand();
                switch (brand) {
                    case "Dell":
                        baseCost = 475;
                        break;
                    case "Lenova":
                        baseCost = 450;
                        break;
                    case "HP":
                        baseCost = 400;
                        break;
                }

                //validation for the radio group of peripherals
                int selectedDesktopPeripheralsId = desktopPeripherals.getCheckedRadioButtonId();
                if (selectedDesktopPeripheralsId != -1){
                    RadioButton selectedDesktopPeripheral = findViewById(selectedDesktopPeripheralsId);
                    String peripherals = selectedDesktopPeripheral.getText().toString();
                    invoice.setPeripherals(peripherals);

                    if(selectedDesktopPeripheralsId == R.id.webcamRadioButton) {
                        baseCost += 95;
                    }
                    else if(selectedDesktopPeripheralsId == R.id.externalDriveRadioButton) {
                        baseCost += 64;
                    }

                }
                else{
                    Toast.makeText(view.getContext(), "Please select one peripheral", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        } else {
            Toast.makeText(view.getContext(), "Please select a computer type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //for the validation of the additional
    private double validateAdditional(){
        ssdCheckBox = findViewById(R.id.ssdCheckBox);
        printerCheckBox = findViewById(R.id.printerCheckBox);

        double additionalCost = 0.0;
        StringBuilder selectedCheckBoxes = new StringBuilder();

        // Check if SSD checkbox is checked
        if (ssdCheckBox.isChecked()) {
            selectedCheckBoxes.append(ssdCheckBox.getText().toString()).append(", ");
            additionalCost += 60;
        }

        // Check if Printer checkbox is checked
        if (printerCheckBox.isChecked()) {
            selectedCheckBoxes.append(printerCheckBox.getText().toString()).append(", ");
            additionalCost += 100;
        }

        // Convert the StringBuilder to a String
        String additional = selectedCheckBoxes.toString();

        // Remove trailing comma and space if any
        if (additional.endsWith(", ")) {
            additional = additional.substring(0, additional.length() - 2);
        }

        //if none of the checkbox is selected
        if(additionalCost == 0){
            additional = "None";
        }

        invoice.setAdditional(additional);

        return additionalCost;
    }

}