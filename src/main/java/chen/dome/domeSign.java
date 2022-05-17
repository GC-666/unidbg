package chen.dome;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class domeSign extends AbstractJni {
    private final AndroidEmulator emulator;
    private final Module module;
    private final VM vm;

    private final DvmClass Native;

    public domeSign() {
        // 创建模拟器实例，要模拟32位或者64位，在这里区分     包名随便
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.qidian.dldl.official").build();
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23));// 设置系统类库解析
        vm = emulator.createDalvikVM(null); // 创建Android虚拟机
        vm.setJni(this);
        vm.setVerbose(true);// 设置是否打印Jni调用细节
        DalvikModule dm = vm.loadLibrary(new File("src/main/resources/libm4399.so"), false);// 加载libcms.so到unicorn虚拟内存，加载成功以后会默认调用init_array等函数
        dm.callJNI_OnLoad(emulator);// 手动执行JNI_OnLoad函数
        module = dm.getModule();// 加载好对应为一个模块
        Native = vm.resolveClass("com/m4399/framework/helpers/AppNativeHelper");//调用resolveClass解析该class对象   .替换为/
    }
    public String crack1(String data) {
        // 调用so的入口，这是smali写法
        String method= "desCbcEncrypt(Ljava/lang/String;)Ljava/lang/String;";
        System.out.println(data);
        StringObject dvmObject = Native.callStaticJniMethodObject(emulator, method, data);
        //销毁模拟器
        this.destroy();
        return dvmObject.toString();
    }
    public void destroy()  {
        try {
            emulator.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
