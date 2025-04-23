export const environment = {
  production: false,
  rootUrl: window[<any>'env'][<any>'rootUrl'] || 'default',
  apiUrl: window[<any>'env'][<any>'apiUrl'] || 'default',
};
