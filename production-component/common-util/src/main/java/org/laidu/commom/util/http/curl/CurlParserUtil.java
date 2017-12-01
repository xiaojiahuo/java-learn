package org.laidu.commom.util.http.curl;

import jodd.util.StringUtil;
import jodd.util.URLDecoder;
import lombok.val;
import org.laidu.commom.util.regex.RegexUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * curl example (brup suite) :
 * curl -i -s -k  -X $'POST' \
 * -H $'Content-Type: application/x-www-form-urlencoded' -H $'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36' \
 * --data-binary $'client_id=77185425430.apps.googleusercontent.com&client_secret=OTJgUOQcT7lO7GsGZq2G4IlT&grant_type=refresh_token&refresh_token=1/YBcVnG-meman9MWGGnYSTaAjhWTPsJr56Y9Dvdcq7U-ezbiSqz23f43SaTRiBOnY&scope=https://www.googleapis.com/auth/cusco-chrome-extension' \
 * $'https://www.googleapis.com/oauth2/v4/token'
 * <p>
 * <p>
 * curl -i -s -k  -X $'POST' \
 * -H $'Content-Type: application/x-www-form-urlencoded' -H $'User-Agent: Dalvik/2.1.0 (Linux; U; Android 6.0.1; Redmi 3 Build/M4B30X)' \
 * --data-binary $'pcn=com.zhaoqianhua.cash&screen=%28720%2C1280%29&dpi=%28320%2C320%29&ak=Xz4E3L0mcVhwhAktoDoHCXbtOAsKPG1L&macaddr=MDI6MDA6MDA6MDA6MDA6MDA%3D%0A++++&url=https%3A%2F%2Fapi.map.baidu.com%2Fsdkcs%2Fverify&appid=-1&mb=Redmi+3&from_service=lbs_androidsdk&net=0&from=lbs_yunsdk&os=Android23&sv=4.0.0&imt=1&output=json&cuid=F663AF9A1E6D8107A06B4AB7DFC5BD38%7C87374814251&mcode=FA%3A8A%3AD5%3AE1%3A57%3A30%3A03%3A1D%3A48%3A2E%3A44%3A9B%3AFB%3AF2%3AEE%3A7A%3ABE%3AE5%3AED%3AFF%3Bcom.zhaoqianhua.cash&resid=02&version=1.0.6&name=%E6%89%BE%E9%92%B1%E8%8A%B1&language=zh&ver=1' \
 * $'https://api.map.baidu.com/sdkcs/verify'
 */
public final class CurlParserUtil {


    /**
     * Private constructor.
     */
    private CurlParserUtil() {
    }

    /**
     * @return Singleton instance
     */
    public static CurlParserUtil getInstance() {
        return HelperHolder.INSTANCE;
    }

    /**
     * Provides the lazy-loaded Singleton instance.
     */
    private static class HelperHolder {
        private static final CurlParserUtil INSTANCE =
                new CurlParserUtil();
    }

    private Map<String, String> keyValuePairs(String curlLine) {
        Map<String, String> keyValuePairs = new HashMap<>();
        String HEADE_PATTERN = "(-H \\$'([^:]*:[^']*)')+";
        int HEAD_DATA_INDEX = 2;
        List<String> headerList = RegexUtil.getInstance().getMacthAllResult(HEADE_PATTERN, curlLine, HEAD_DATA_INDEX);
        headerList.forEach(header ->{
            String[] keyValue = header.split(":", 2);
            if (keyValue.length == 2) {
                keyValuePairs.put(keyValue[0].trim(), URLDecoder.decode(keyValue[1].trim()));
            }
        });

        return keyValuePairs;
    }

    Map<String, String> getHeaders(String curlLine) {

        Map<String, String> keyValuePairs = keyValuePairs(curlLine);

        keyValuePairs.remove("Content-Length");

        return keyValuePairs;
    }

    Map<String, String> getCookies(String curlLine) {

        Map<String, String> cookieMap = new HashMap<>();

        StringBuilder cookiesString = new StringBuilder();

        cookiesString.append(getHeaders(curlLine).get("Cookie"));
        cookiesString.append(getBCookiesString(curlLine));

        if (StringUtil.isNotBlank(cookiesString.toString())) {
            String cookiesPattern = "([^; ]*=[^;]*)+";
            List<String> cookieStringList = RegexUtil.getInstance().getMacthAllResult(cookiesPattern,cookiesString.toString());

            cookieStringList.forEach(cookie -> {
                String[] cookiePairs = cookie.split("=", 2);
                if (cookiePairs.length == 2) {
                    cookieMap.put(cookiePairs[0].trim(), URLDecoder.decode(cookiePairs[1].trim()));
                }
            });
        }

        return cookieMap;
    }

    private String getBCookiesString(String curlLine) {

        String b_HEADER_PATTRERN = "-b \\$'([^']*)'";
        int b_HEADER__INDEX = 1;
        return RegexUtil.getInstance().getMacthResult(b_HEADER_PATTRERN, curlLine, b_HEADER__INDEX);
    }


    String getMethod(String curlLine) {
        String METHOD_OATTERN = "-X \\$'([A-Z]{2,5})'";
        int METHOD_INDEX = 1;
        return RegexUtil.getInstance().getMacthResult(METHOD_OATTERN, curlLine, METHOD_INDEX, "GET");
    }


    String getUrl(String curlLine) {
        String URL_PATTERN = "\\$'(https?://.*)'";
        int URL_INDEX = 1;
        return RegexUtil.getInstance().getMacthResult(URL_PATTERN, curlLine, URL_INDEX);
    }

    String getBodyString(String curlLine) {
        String STRING_BODY_PATTERN = "--data-binary \\$'(.*)'";
        int STRING_BODY_INDEX = 1;
        return RegexUtil.getInstance().getMacthResult(STRING_BODY_PATTERN, curlLine, STRING_BODY_INDEX);
    }

    Map<String, String> getFormBody(String curlLine) {
        val formMap = new HashMap<String, String>();

        String formBodyString = getBodyString(curlLine);

        String FORM_DATA_PATTERN = "([^=&]+=[^&']*)+";
        int FORM_DATA_INDEX = 1;
        List<String> formStringList = RegexUtil.getInstance().getMacthAllResult(FORM_DATA_PATTERN, formBodyString, FORM_DATA_INDEX);

        formStringList.forEach(formString -> {

            String[] form = formString.split("=",2);
            if (form.length==2) {
                formMap.put(form[0].trim(),form[1].trim());
            }
        });

        return formMap;
    }


}

