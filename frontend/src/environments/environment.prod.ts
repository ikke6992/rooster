export const environment = {
  production: true,
  rootUrl: window[<any>'env'][<any>'rootUrl'] || 'default',
  apiUrl: window[<any>'env'][<any>'apiUrl'] || 'default',
};
