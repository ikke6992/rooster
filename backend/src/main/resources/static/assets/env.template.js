(function (window) {
  window.env = window.env || {};

  window["env"]["apiUrl"] = `${API_URL}`;
  window["env"]["rootUrl"] = `${ROOT_URL}`;
  window["env"]["debug"] = false;
})(this);
