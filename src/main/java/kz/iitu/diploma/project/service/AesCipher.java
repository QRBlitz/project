package kz.iitu.diploma.project.service;

public interface AesCipher {

    String encrypt(byte[] key, String data) throws Exception;
    String decrypt(String key, String encryptedData) throws Exception;

}
