package nova.module.modules;

import net.minecraft.util.ChatAllowedCharacters;
import nova.Nova;
import nova.core.Base64;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.ChatRecievedEvent;
import nova.event.events.ChatSentEvent;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;

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

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleEncryption extends ModuleBase
{
    // TODO: rewrite all of this, make encryption method private to git

    @Saveable
    String delimiter;

    public HashMap a;
    private byte[] d;
    private byte[] e;
    private int f;
    private SecretKeySpec g;
    private IvParameterSpec h;
    public Queue b;
    public int c;

    public ModuleEncryption() {
        super();
        this.isToggleable = false;

        this.description = ("encrypts chats");

        this.defaultArg = "delimiter";

        a = new HashMap();
        d = new byte[] { 11, 90, 27, 71, -45, 126, -61, -7, -56, 105, 106, -56, -52, 2, 114, 3 };
        e = new byte[] { 120, -95, -6, -62, -120, -1, -61, -70, 36, -128, 61, 34, 39, -78, 56, 94 };
        f = 32;
        g = new SecretKeySpec(d, "AES");
        h = new IvParameterSpec(e);
        b = new LinkedList();
        c = 0;

        this.delimiter = "%";
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
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            localCipher.init(2, g, h);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] arrayOfByte2 = null;

        try {
            arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        byte[] arrayOfByte3 = new byte[arrayOfByte2.length - 16];

        for (int j = 0; j < arrayOfByte3.length; j++)
            arrayOfByte3[j] = arrayOfByte2[(j + 16)];

        return new String(arrayOfByte3);
    }

    private byte[] EncryptData(String paramString)
    {
        byte[] arrayOfByte1 = paramString.getBytes();
        byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 16];

        for (int i = 0; i < arrayOfByte1.length; i++)
            arrayOfByte2[(i + 16)] = arrayOfByte1[i];

        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        Cipher localCipher = null;

        try {
            localCipher = Cipher.getInstance("AES/CFB/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
            e1.printStackTrace();
        }

        try {
            localCipher.init(1, g, h);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            localByteArrayOutputStream.write(localCipher.doFinal(arrayOfByte2));
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }

        byte[] arrayOfByte3 = localByteArrayOutputStream.toByteArray();

        try {
            localByteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfByte3;
    }

    private byte[] DecodeBase64(String paramString)
    {
        return Base64.decodeToBytes(paramString);
    }

    private String EncodeBase64(byte[] paramArrayOfByte)
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

                mc.player.sendChatMessage(str);

        }
    }

    @RegisterArgument(name = "delimiter", description = "what to start a chat with to encrypt it, eg. %")
    public void changeDelimiter(String del) {
        this.delimiter = del;
        Nova.confirmMessage("Delimiter changed to: " + del);
    }

    @EventHandler
    public boolean onChatSent(ChatSentEvent e)
    {
        String message = e.getMessage();

        return encryptMessage(message);
    }

    public boolean encryptMessage(String message){
        if (!(message.startsWith(this.delimiter)))
            return true;

        String name = mc.player.getDisplayName().getUnformattedText();
        System.out.print(name);

        String nameHash = Util.hash(name);

        try
        {
            int j = b.size() == 0 ? 1 : 0;

            // This is where color codes are
            String str3 = EncodeBase64(EncryptData(message.substring(1).replace(this.delimiter, "\247")));

            str3 = str3.replace("\r", "");
            str3 = str3.replace("\n", "");


            String str4 = nameHash + str3 + "\\\\";
            if (str4.length() > 99 - nameHash.length() - 2)
            {
                StringBuilder localStringBuilder = new StringBuilder();
                for (int k = 0; k < str3.length(); k++)
                {
                    char c1 = str3.charAt(k);
                    localStringBuilder.append(c1);

                    if ((ChatAllowedCharacters.isAllowedCharacter(c1)) && (localStringBuilder.length() <= 99 - nameHash.length() - 2))
                        continue;

                    String str7 = nameHash + localStringBuilder.toString();
                    b.add(str7);
                    localStringBuilder = new StringBuilder();
                }

                localStringBuilder.append("\\\\");
                String str5 = nameHash + localStringBuilder.toString();
                b.add(str5);
                if (j == 0)
                    return true;
                String str6 = (String)b.poll();

                mc.player.sendChatMessage(str6);
                return false;
            }
            if (j == 0)
            {
                b.add(str4);
                return true;
            }
            mc.player.sendChatMessage(str4);

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
                str3 = "\2475" + username + ": \247r" + str3 + "\247r";
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
