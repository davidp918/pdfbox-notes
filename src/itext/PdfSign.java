package itext;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;

public interface PdfSign {
    void sign(InputStream src // 需要签章的pdf文件路径
            , OutputStream dest // 签完章的pdf文件路径
            , Certificate[] chain // 证书链
            , PrivateKey pk // 签名私钥
            , String digestAlgorithm // 摘要算法名称，例如SHA-1
            , String provider // 密钥算法提供者，可以为null
            , CryptoStandard subfilter // 数字签名格式，itext有2种
            , String reason // 签名的原因，显示在pdf签名属性中，随便填
            , String location, String chapterPath); // 签名的地点，显示在pdf签名属性中，随便填
}
