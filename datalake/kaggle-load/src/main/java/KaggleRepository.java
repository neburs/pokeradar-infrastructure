import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;

public class KaggleRepository
{
    public void downloadDataSet()
    {
        URL dataSet = new URL("https://www.kaggle.com/semioniy/predictemall/downloads/300k_csv.zip");
        ReadableByteChannelleByteChannel rbc = Channels.newChannel(dataSet.openStream());
        FileOutputStream fos = new FileOutputStream("300k_csf.zip");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
