export const APP_TOKEN = "app_token";

interface AuthTokenStorageInterface {
  getToken: () => string;
  // eslint-disable-next-line no-unused-vars
  saveToken: (token: string) => void;
  removeToken: () => void;
  isTokenPresent: () => boolean;
}

export class AuthTokenStorage implements AuthTokenStorageInterface {
  private token: string;

  constructor() {
    this.token = localStorage.getItem(APP_TOKEN) || "";
  }

  getToken(): string {
    return this.token;
  }

  saveToken(token: string): void {
    this.token = token;
    localStorage.setItem(APP_TOKEN, token);
  }

  removeToken(): void {
    localStorage.removeItem(APP_TOKEN);
  }

  isTokenPresent(): boolean {
    return !!this.token.length;
  }
}
