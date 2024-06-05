import CryptoJS from 'crypto-js';

export const encrypt = (plaintext, secret) => {
    var key = CryptoJS.enc.Utf8.parse(secret);
    var iv = CryptoJS.lib.WordArray.random(16);
    console.log("IV : " + CryptoJS.enc.Base64.stringify(iv));

    var cipherText = CryptoJS.AES.encrypt(plaintext, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });

    var combined = iv.concat(cipherText.ciphertext);
    return combined.toString(CryptoJS.enc.Base64);
}

export const decrypt = (cipherText, secret) => {
    var key = CryptoJS.enc.Utf8.parse(secret);


    var combined = CryptoJS.enc.Base64.parse(cipherText);
    var iv = combined.clone().words.slice(0, 4);
    var cipherBytes = combined.words.slice(4);

    var decrypted = CryptoJS.AES.decrypt({
        ciphertext: CryptoJS.lib.WordArray.create(cipherBytes)
    }, key, {
        iv: CryptoJS.lib.WordArray.create(iv),
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });

    return decrypted.toString(CryptoJS.enc.Utf8);
}
