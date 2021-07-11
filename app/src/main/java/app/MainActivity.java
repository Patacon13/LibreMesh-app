package app;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.limeapp.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;

    public boolean httpGetToLibreMesh() throws InterruptedException, IOException {
        //FIXME: modificar google por la IP de LibreMesh
        String[] cmdLine = {"sh", "-c", "curl --head --silent --fail thisnode.info/app"};
        System.out.println(cmdLine[0] + cmdLine[1] + cmdLine[2]);
        Process p1 = java.lang.Runtime.getRuntime().exec(cmdLine);
        int returnVal = p1.waitFor();
        return (returnVal == 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    public boolean verifyLibreMeshConnection() {
        try {
            return httpGetToLibreMesh();
        } catch (InterruptedException e) {
            System.out.println("Error de interrupcion al intentar acceder a la IP de LibreMesh.");
        } catch (IOException e) {
            System.out.println("Error de entrada o salida al intentar acceder a la IP de LibreMesh.");
        }
        return false;

    }

    public void informConnectionToLibreMesh(View view) {
        if(WifiInformationManager.verifyWifiConnection(wifiManager))
            Toast.makeText(getApplicationContext(), verifyLibreMeshConnection() ? "Está en una red LibreMesh" : "No está en una red LibreMesh",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "No está conectado a la WiFi. Conéctese y vuelva a intentarlo" ,Toast.LENGTH_LONG).show();
    }

    public void informPrivateIp(View view) {
        Toast.makeText(getApplicationContext(), WifiInformationManager.getPrivateIp(wifiManager),Toast.LENGTH_LONG).show();
    }


    public void accessToLibreMesh(View view) {
        Intent myIntent = new Intent(this, LibreMesh.class);
        startActivity(myIntent);
        if(WifiInformationManager.verifyWifiConnection(wifiManager))
            if(verifyLibreMeshConnection())
                startActivity(myIntent);
            else
                Toast.makeText(getApplicationContext(), "No está en una red LibreMesh",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "No está conectado a la WiFi. Conéctese y vuelva a intentarlo" ,Toast.LENGTH_LONG).show();
    }
}