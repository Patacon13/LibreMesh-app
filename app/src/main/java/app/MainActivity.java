package app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.limeapp.R;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    public boolean haceHttpGetALibreMesh() throws InterruptedException, IOException {
        //FIXME: modificar google por la IP de LibreMesh
        String[] cmdLine = {"sh", "-c", "curl --head --silent --fail google.com"};
        Process p1 = java.lang.Runtime.getRuntime().exec(cmdLine);
        int returnVal = p1.waitFor();
        return returnVal == 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean verificaConexionALibreMesh() {
        try {
            return haceHttpGetALibreMesh();
        } catch (InterruptedException e) {
            System.out.println("Error de interrupcion al intentar acceder a la IP de LibreMesh.");
        } catch (IOException e) {
            System.out.println("Error de entrada o salida al intentar acceder a la IP de LibreMesh.");
        }
        return false;

    }

    public String getIpPrivada() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        return ipString;
    }

    public boolean verificaConexionPorWiFi() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wm.isWifiEnabled()) {
            return (wm.getConnectionInfo().getNetworkId() == -1) ? false : true;
        }
        return false;
    }

    public void informaConexionALibreMesh(View view) {
        if(verificaConexionPorWiFi())
            Toast.makeText(getApplicationContext(), verificaConexionALibreMesh() ? "Está en una red LibreMesh" : "No está en una red LibreMesh",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "No está conectado a la WiFi. Conéctese y vuelva a intentarlo" ,Toast.LENGTH_LONG).show();
    }

    public void informaIpPrivada(View view) {
        Toast.makeText(getApplicationContext(), getIpPrivada(),Toast.LENGTH_LONG).show();
    }
}