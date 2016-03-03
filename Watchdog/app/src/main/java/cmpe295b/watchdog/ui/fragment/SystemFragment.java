package cmpe295b.watchdog.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

//import org.apache.http.conn.InetAddressUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import cmpe295b.watchdog.R;
import cmpe295b.watchdog.core.WatchdogApplication;
import cmpe295b.watchdog.ui.activity.BaseActivity;
import timber.log.Timber;


public class SystemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_system, container, false);

        // Get tracker.
        Tracker t = ((WatchdogApplication) getActivity().getApplication()).getTracker(WatchdogApplication.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("SystemFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        // Wifi Service
        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String wifiString = wifiInfo.getMacAddress();

        //get elements
        TextView textViewSystemWifiMac = (TextView) view.findViewById(R.id.textview_system_wifi_mac);
        TextView textViewSystemWifiIpv4 = (TextView) view.findViewById(R.id.textview_system_wifi_ipv4);
        TextView textViewSystemWifiIpv6 = (TextView) view.findViewById(R.id.textview_system_wifi_ipv6);
        TextView textViewSystemCpu = (TextView) view.findViewById(R.id.textview_system_wifi_cpu);
        TextView textTotalCpuUsage = (TextView) view.findViewById(R.id.textview_Total_cpu_usage);

        //set elements
        textViewSystemWifiMac.setText(wifiInfo.getMacAddress());
        textViewSystemWifiIpv4.setText(getIPAddress(true));
        textViewSystemWifiIpv6.setText(getIPAddress(false));
        textViewSystemCpu.setText(readCpuInfo());
        textTotalCpuUsage.setText("Total CPU Utillization "+getCpuUsageStatistic().toString());
        //textTotalCpuUsage.setText("sdadadsa");

        Timber.d("cpu:"+readCpuInfo());

        return view;
    }

    private String getCpuUsageStatistic() {

        String tempString = executeTop();

        /*tempString = tempString.replaceAll(",", "");
        tempString = tempString.replaceAll("User", "");
        tempString = tempString.replaceAll("System", "");
        tempString = tempString.replaceAll("IOW", "");
        tempString = tempString.replaceAll("IRQ", "");
        tempString = tempString.replaceAll("%", "");
        for (int i = 0; i < 10; i++) {
            tempString = tempString.replaceAll("  ", " ");
        }
        tempString = tempString.trim();
        String[] myString = tempString.split(" ");
        int[] cpuUsageAsInt = new int[myString.length];
        for (int i = 0; i < myString.length; i++) {
            myString[i] = myString[i].trim();
            cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
        }*/
        return tempString;
    }

    private String executeTop() {
        java.lang.Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("top -n 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        return returnString;
    }





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((BaseActivity) activity).onSectionAttached(2);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = isValidIp4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return "";
        } // for now eat exceptions
        return "";
    }

    public static boolean isValidIp4Address(final String hostName) {
        try {
            return Inet4Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }


    private String readCpuInfo()
    {
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
}
