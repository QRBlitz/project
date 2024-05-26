from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
import os
import argparse

def aes_encrypt(message, key):
    iv = os.urandom(16)
    cipher = Cipher(algorithms.AES(key), modes.CFB(iv), backend=default_backend())
    encryptor = cipher.encryptor()
    ct = encryptor.update(message.encode()) + encryptor.finalize()
    return iv + ct

def aes_decrypt(ciphertext, key):
    iv = ciphertext[:16]
    ct = ciphertext[16:]
    cipher = Cipher(algorithms.AES(key), modes.CFB(iv), backend=default_backend())
    decryptor = cipher.decryptor()
    return decryptor.update(ct) + decryptor.finalize()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Encrypt and decrypt using AES")
    parser.add_argument("mode", choices=["encrypt", "decrypt"], help="Mode: 'encrypt' or 'decrypt'")
    parser.add_argument("key", help="AES encryption/decryption key")
    parser.add_argument("message", help="Message to encrypt/decrypt")

    args = parser.parse_args()

    if args.mode == "encrypt":
        result = aes_encrypt(args.message, args.key.encode('utf-8'))
    else:
        result = aes_decrypt(args.message.encode(), args.key)

    print(result)