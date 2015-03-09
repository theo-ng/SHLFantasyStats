import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class GoogleSheetUpdate {
  public static void main(String[] args)
      throws AuthenticationException, MalformedURLException, IOException, ServiceException {

    // Application code here
	  SpreadsheetService service = new SpreadsheetService("GoogleSheetUpdate");
  }
}
