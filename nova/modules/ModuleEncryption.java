package nova.modules;

import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.util.ChatAllowedCharacters;
import nova.Command;
import nova.core.Base64;
import nova.core.Util;
import nova.events.ChatSentEvent;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;
import nova.events.ChatRecievedEvent;

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleEncryption extends ModuleBase
{
    @Expose
    String delimeter;

    public HashMap a;
    private byte[] d;
    private byte[] e;
    private int f;
    private SecretKeySpec g;
    private IvParameterSpec h;
    public Queue b;
    public int c;

    public ModuleEncryption(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        this.name = "Encryption";
        this.isToggleable = false;

        this.command = new Command(Nova, this, aliases, "encrypts chats");
        this.command.registerArg("delimeter", this.getClass().getMethod("changeDelimeter", String.class), "what to start a chat with to encrypt it, ex. %");

        this.defaultArg = "delimeter";

        a = new HashMap();
        d = new byte[] { 11, 90, 27, 71, -45, 126, -61, -7, -56, 105, 106, -56, -52, 2, 114, 3 };
        e = new byte[] { 120, -95, -6, -62, -120, -1, -61, -70, 36, -128, 61, 34, 39, -78, 56, 94 };
        f = 32;
        g = new SecretKeySpec(d, "AES");
        h = new IvParameterSpec(e);
        b = new LinkedList();
        c = 0;

        this.delimeter = "%";

        loadModule();
    }

    @Override
    public void saveModule(){
        json.add("delimeter", Util.getGson().toJsonTree(delimeter));
        super.saveModule();
    }

    @Override
    public void load(){
        super.load();
        delimeter = Util.getGson().fromJson(json.get("delimeter"), String.class);
    }

    public String DecryptData(byte[] paramArrayOfByte)
    {
        ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
        int i = paramArrayOfByte.length;
        byte[] arrayOfByte1 = new byte[i];
        localByteBuffer.get(arrayOfByte1);
        Cipher localCipher = null;

        try {
            localCipher = Cipher.getInstance("AES/CFB/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            localCipher.init(2, g, h);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] arrayOfByte2 = null;
        try {
            arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] arrayOfByte3 = new byte[arrayOfByte2.length - 16];

        for (int j = 0; j < arrayOfByte3.length; j++)
            arrayOfByte3[j] = arrayOfByte2[(j + 16)];

        return new String(arrayOfByte3);
    }

    public byte[] EncryptData(String paramString)
    {
        byte[] arrayOfByte1 = paramString.getBytes();
        byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 16];

        for (int i = 0; i < arrayOfByte1.length; i++)
            arrayOfByte2[(i + 16)] = arrayOfByte1[i];

        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        Cipher localCipher = null;

        try {
            localCipher = Cipher.getInstance("AES/CFB/NoPadding");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            localCipher.init(1, g, h);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            localByteArrayOutputStream.write(localCipher.doFinal(arrayOfByte2));
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] arrayOfByte3 = localByteArrayOutputStream.toByteArray();
        try {
            localByteArrayOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayOfByte3;
    }

    public byte[] DecodeBase64(String paramString)
    {
        return Base64.decodeToBytes(paramString);
    }

    public String EncodeBase64(byte[] paramArrayOfByte)
    {
        return Base64.encodeToString(paramArrayOfByte);
    }


    @EventHandler
    public void onTick(PlayerTickEvent e)
    {
        c += 1;
        if (c > 60)
        {
            c = 0;
            String str = (String)b.poll();
            if (str != null)

                mc.thePlayer.sendChatMessage(str);

        }
    }


    public void changeDelimeter(String del){
        this.delimeter = del;
        this.Nova.confirmMessage("Delimeter changed to: " + del);
    }

    @EventHandler
    public boolean onChatSent(ChatSentEvent e)
    {
        String message = e.getMessage();

        return encryptMessage(message);
    }

    public boolean encryptMessage(String message){
        if (!(message.startsWith(this.delimeter)))
            return true;

        String str1 = mc.thePlayer.getDisplayName().getUnformattedText();
        System.out.print(str1);

        String str2 = Util.hash(str1);

        try
        {
            int j = b.size() == 0 ? 1 : 0;
            String str3 = EncodeBase64(EncryptData(message.substring(1)));

            str3 = str3.replace("\r", "");
            str3 = str3.replace("\n", "");
            String str4 = str2 + str3 + "\\\\";
            if (str4.length() > 99 - str2.length() - 2)
            {
                StringBuilder localStringBuilder = new StringBuilder();
                for (int k = 0; k < str3.length(); k++)
                {
                    char c1 = str3.charAt(k);
                    localStringBuilder.append(c1);

                    if ((ChatAllowedCharacters.isAllowedCharacter(c1)) && (localStringBuilder.length() <= 99 - str2.length() - 2))
                        continue;

                    String str7 = str2 + localStringBuilder.toString();
                    b.add(str7);
                    localStringBuilder = new StringBuilder();
                }

                localStringBuilder.append("\\\\");
                String str5 = str2 + localStringBuilder.toString();
                b.add(str5);
                if (j == 0)
                    return true;
                String str6 = (String)b.poll();

                mc.thePlayer.sendChatMessage(str6);
                return false;
            }
            if (j == 0)
            {
                b.add(str4);
                return true;
            }
            mc.thePlayer.sendChatMessage(str4);

            return false;
        }
        catch (Exception localException)
        {
        }
        return true;
    }

    @EventHandler
    public boolean onChatReceived(ChatRecievedEvent e)
    {		String username;
        String message;
        String paramString3;

        message = e.getMessage();
        username = message.split(">")[0];
        username = username.substring(1);

        if(1 < message.split(">").length)
            message = message.split(">")[1].substring(1);

        String str1 = (String)a.get(username);
        if ((str1 == null) || (str1.length() == 0))
            str1 = "";

        String str2 = Util.hash(username);


        if (!message.startsWith(str2))
            return true;

        message = message.substring(str2.length());
        str1 = str1 + message;


        if (str1.endsWith("\\\\"))
        {
            int j = 0;
            int k = str1.length() - 2;
            str1 = str1.substring(j, k);

            try
            {
                String str3 = DecryptData(DecodeBase64(str1));
                str3 = "\2473" + username + ": \247r" + str3 + "\247r";
                a.put(username, "");

                Nova.message(str3);

                return false;
            }
            catch (Exception localException)
            {
                a.put(username, "");
                return !(str1.length() <= 99);
            }
        }
        a.put(username, str1);
        return false;
    }
}
