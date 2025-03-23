import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';
import { environment } from '../../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  storeEncryptedToken(key: string, value: string): void {
    const encryptedToken = CryptoJS.AES.encrypt(value, environment.secretKey).toString();
    localStorage.setItem(key, encryptedToken);
  }

  getDecryptedToken(): string | null {
    const encryptedToken = localStorage.getItem('accessToken');
    if (!encryptedToken) return null;

    try {
      const bytes = CryptoJS.AES.decrypt(encryptedToken, environment.secretKey);
      return bytes.toString(CryptoJS.enc.Utf8);
    } catch (error) {
      console.error('Error decrypting token:', error);
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem('accessToken');
  }
}
