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

public class MainActivity extends AppCompatActivity {

    EditText customerNameText;
    AutoCompleteTextView provinceText;
    RadioGroup computerTypeRadioGroup, laptopPeripherals, desktopPeripherals;
    Spinner spinner;
    CheckBox ssdCheckBox, printerCheckBox;
    TextView invoiceText;

    List<String> provinces = Arrays.asList(
            "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
            "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan"
    );

    String customerName, province, computerType, brand, additional, peripherals;
    double baseCost = 0;
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
        computerTypeRadioGroup = findViewById(R.id.computerTypeRadioGroup);
        laptopPeripherals = findViewById(R.id.laptopPeripherals);
        desktopPeripherals = findViewById(R.id.desktopPeripherals);
        computerTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.desktopRadioButton) {
                    desktopPeripherals.setVisibility(View.VISIBLE);
                    laptopPeripherals.setVisibility(View.GONE);
                }
                else if (checkedId == R.id.laptopRadioButton) {
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
                brand = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //arraylist for viewing brands in the spinner
        ArrayList<String> brands = new ArrayList<>();
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
                //invoice as a multi-line text
                invoiceText = findViewById(R.id.invoiceText);
                invoiceText.setText(""); //clears the invoice

                //validation of the entered customer name
                customerName = customerNameText.getText().toString().trim();
                if(customerName.isEmpty()){
                    Toast.makeText(view.getContext(), "Customer Name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validations of the entered province name
                province = provinceText.getText().toString().trim();
                if(province.isEmpty()){
                    Toast.makeText(view.getContext(), "Province Name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(provinces.contains(province))) {
                    Toast.makeText(view.getContext(), "Invalid Province Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validation for the radio group of computer type and peripherals
                int selectedRadioButtonId = computerTypeRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) { // Check if any radio button is selected
                    RadioButton selectedComputerType = findViewById(selectedRadioButtonId);
                    computerType = selectedComputerType.getText().toString();
                    if(computerType.equals("Laptop")){
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

                        int selectedLaptopPeripheralsId = laptopPeripherals.getCheckedRadioButtonId();
                        if (selectedLaptopPeripheralsId != -1){
                            RadioButton selectedLaptopPeripheral = findViewById(selectedLaptopPeripheralsId);
                            peripherals = selectedLaptopPeripheral.getText().toString();

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
                            return;
                        }
                    }

                    if(computerType.equals("Desktop")){
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

                        int selectedDesktopPeripheralsId = desktopPeripherals.getCheckedRadioButtonId();
                        if (selectedDesktopPeripheralsId != -1){
                            RadioButton selectedDesktopPeripheral = findViewById(selectedDesktopPeripheralsId);
                            peripherals = selectedDesktopPeripheral.getText().toString();

                            if(selectedDesktopPeripheralsId == R.id.webcamRadioButton) {
                                baseCost += 95;
                            }
                            else if(selectedDesktopPeripheralsId == R.id.externalDriveRadioButton) {
                                baseCost += 64;
                            }

                        }
                        else{
                            Toast.makeText(view.getContext(), "Please select one peripheral", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } else {
                    Toast.makeText(view.getContext(), "Please select a computer type", Toast.LENGTH_SHORT).show();
                    return;
                }

                //total cost from the additional devices
                double additionalCost = validateAdditional(); //validation for the additional
                baseCost = baseCost + additionalCost;

                //calculate total cost including TAX
                double totalCost = baseCost * ( 1 + TAX_RATE);
                String formattedTotalCost = String.format(Locale.ENGLISH,"$%.2f", totalCost);

                //text to be shown when the Calculate Button is clicked
                String result = "Customer: " + customerName + "\n" +
                        "Province: " + province + "\n" +
                        "Computer: " + computerType + "\n" +
                        "Brand: " + brand + "\n" +
                        "Additional: " + additional + "\n" +
                        "Added Peripherals: " + peripherals + "\n" +
                        "Cost: " + formattedTotalCost;
                invoiceText.setText(result);

            }
        });

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
        additional = selectedCheckBoxes.toString();

        // Remove trailing comma and space if any
        if (additional.endsWith(", ")) {
            additional = additional.substring(0, additional.length() - 2);
        }

        return additionalCost;
    }
}