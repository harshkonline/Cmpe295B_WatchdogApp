package cmpe295b.watchdog.util;

/**
 * Created by harshad on 3/19/2016.
 */
public class QueryBuilder {

    /**
     * Specify your database name here
     * @return
     */
    public String getDatabaseName() {
        return "systeminfo";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey() {
        return "DdQKPEkFz-4rlwpLnc76J2mnvz4QjvJ7";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Returns the systeminfo collection
     * @return
     */
    public String documentRequest()
    {
        return "systeminfo";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return
     */
    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    /**
     * Formats the contact details for MongoHQ Posting
     * @param sysInfo: system info
     * @return
     */
    public String createRecord(SystemInfo sysInfo)
    {
        return String
                .format("{\"document\" : {\"cpuUtil\": \"%s\", }, \"safe\" : true}",
                        sysInfo.totalCPUUtil, sysInfo.totalCPUUtil, sysInfo.totalCPUUtil, sysInfo.totalCPUUtil);
    }

}