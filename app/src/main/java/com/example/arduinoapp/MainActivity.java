package com.example.arduinoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String deviceName = null;
    private String deviceAddress;
    private Button btnExit;
    private Button btnTemperatura, btnMovimiento, btnSonido, btnExitTemp, btnExitSonido, btnExitMovimiento;
    public int option = 0;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Initialization
        btnExit = (Button) findViewById(R.id.btnExit);
        //btnTemperatura = (Button) findViewById(R.id.btnTemperatura);
        //btnSonido = (Button) findViewById(R.id.btnSonido);
        //btnMovimiento = (Button) findViewById(R.id.btnMovimiento);
        btnExitTemp = (Button) findViewById(R.id.btnExitTemp);
        btnExitSonido = (Button) findViewById(R.id.btnExitSonido);
        btnExitMovimiento = (Button) findViewById(R.id.btnExitMovimiento);

        final Button buttonConnect = findViewById(R.id.buttonConnect);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        final TextView lblDescTemp = findViewById(R.id.lblDescTemp);
        final TextView lblTemp = findViewById(R.id.lblTemp);
        final TextView lblTitulo = findViewById(R.id.lblTitulo);
        final TextView lblInstrucciones = findViewById(R.id.lblInstrucciones);
        final TextView lblDescSonido = findViewById(R.id.lblDescSonido);
        final Switch switchSonido = findViewById(R.id.switchSonido);
        final TextView lblDescMovimiento = findViewById(R.id.lblDescMovimiento);
        final Switch switchAlarma = findViewById(R.id.switchAlarma);

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){
            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progree and connection status
            toolbar.setSubtitle("Connecting to " + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            /*
            This is the most important piece of code. When "deviceName" is found
            the code will call a new thread to create a bluetooth connection to the
            selected device (see the thread code below)
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }



        btnTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 1;
                //Change some main screen items as GONE and change the text in title and in description
                lblTitulo.setText("Sensor de temperatura");
                lblInstrucciones.setText("Mide la temperatura de tu hogar");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.GONE);
                btnTemperatura.setVisibility(View.GONE);
                btnSonido.setVisibility(View.GONE);
                btnMovimiento.setVisibility(View.GONE);
                btnExit.setVisibility(View.GONE);

                //Set visibility of temp items as VISIBLE
                lblDescTemp.setVisibility(View.VISIBLE);
                lblTemp.setVisibility(View.VISIBLE);
                btnExitTemp.setVisibility(View.VISIBLE);
            }
        });

        btnExitTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 0;
                //Change some main screen items as Visible and change the text in title and in description.
                lblTitulo.setText("Casa Inteligente");
                lblInstrucciones.setText("Una casa inteligente con \ncaracterísticas remotas.");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.VISIBLE);
                btnTemperatura.setVisibility(View.VISIBLE);
                btnSonido.setVisibility(View.VISIBLE);
                btnMovimiento.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);

                //Set visibility of temp items as GONE
                lblDescTemp.setVisibility(View.GONE);
                switchSonido.setVisibility(View.GONE);
                btnExitTemp.setVisibility(View.GONE);
            }
        });

        btnSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 1;
                //Change some main screen items as GONE and change the text in title and in description
                lblTitulo.setText("Sensor de Sonido");
                lblInstrucciones.setText("Puede encender y apagar las \nluces por medio de aplausos");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.GONE);
                btnTemperatura.setVisibility(View.GONE);
                btnSonido.setVisibility(View.GONE);
                btnMovimiento.setVisibility(View.GONE);
                btnExit.setVisibility(View.GONE);

                //Set visibility of temp items as VISIBLE
                lblDescSonido.setVisibility(View.VISIBLE);
                switchSonido.setVisibility(View.VISIBLE);
                btnExitSonido.setVisibility(View.VISIBLE);
            }
        });

        btnExitSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 2;
                //Change some main screen items as Visible and change the text in title and in description.
                lblTitulo.setText("Casa Inteligente");
                lblInstrucciones.setText("Una casa inteligente con \ncaracterísticas remotas.");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.VISIBLE);
                btnTemperatura.setVisibility(View.VISIBLE);
                btnSonido.setVisibility(View.VISIBLE);
                btnMovimiento.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);

                //Set visibility of temp items as GONE
                lblDescSonido.setVisibility(View.GONE);
                switchSonido.setVisibility(View.GONE);
                btnExitSonido.setVisibility(View.GONE);
            }
        });

        btnMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 3;
                //Change some main screen items as GONE and change the text in title and in description
                lblTitulo.setText("Sensor de Movimiento");
                lblInstrucciones.setText("Puede ver el estado de la alarma");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.GONE);
                btnTemperatura.setVisibility(View.GONE);
                btnSonido.setVisibility(View.GONE);
                btnMovimiento.setVisibility(View.GONE);
                btnExit.setVisibility(View.GONE);

                //Set visibility of temp items as VISIBLE
                lblDescMovimiento.setVisibility(View.VISIBLE);
                switchAlarma.setVisibility(View.VISIBLE);
                btnExitMovimiento.setVisibility(View.VISIBLE);
            }
        });

        btnExitMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 0;
                //Change some main screen items as Visible and change the text in title and in description.
                lblTitulo.setText("Casa Inteligente");
                lblInstrucciones.setText("Una casa inteligente con \ncaracterísticas remotas.");
                progressBar.setVisibility(View.GONE);
                buttonConnect.setVisibility(View.VISIBLE);
                btnTemperatura.setVisibility(View.VISIBLE);
                btnSonido.setVisibility(View.VISIBLE);
                btnMovimiento.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);

                //Set visibility of temp items as GONE
                lblDescMovimiento.setVisibility(View.GONE);
                switchAlarma.setVisibility(View.GONE);
                btnExitMovimiento.setVisibility(View.GONE);
            }
        });

        /*
        Second most important piece of Code. GUI Handler
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                toolbar.setSubtitle("Connected to " + deviceName);
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                break;
                            case -1:
                                toolbar.setSubtitle("Device fails to connect");
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        if (option == 1) {
                            if (arduinoMsg == "Intrusos detectados") {
                                lblDescMovimiento.setText("Se han detectado intrusos");
                            }
                        } else if (option == 2) {
                            if (arduinoMsg == "LED ON") {
                                lblDescSonido.setText("Los focos se encuentran encendidos");
                            }
                        } else if (option == 3) {
                            lblTemp.setText(" " +arduinoMsg + " Grados");
                        }
                        break;
                }
            }
        };

        // Select Bluetooth Device
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to adapter list
                Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                startActivity(intent);
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder confirmar = new AlertDialog.Builder(MainActivity.this);
                confirmar.setTitle("¿Cerrar la aplicación?");
                confirmar.setMessage("¿Está seguro de que salir de la aplicación?");
                confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                confirmar.show();
            }
        });
    }

    //Metodo para el boton de temperatura
    public void Temperatura(View view){
        Toast.makeText(this, "Sensor de temperatura", Toast.LENGTH_SHORT).show();
    }

    //Metodo para el boton de sonido
    public void Sonido(View view){
        Toast.makeText(this, "Sensor de sonido", Toast.LENGTH_SHORT).show();
    }

    //Metodo para el boton de Movimiento
    public void Movimiento(View view){
        Toast.makeText(this, "Sensor de Movimiento", Toast.LENGTH_SHORT).show();
    }

    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Arduino Message",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}