let stompClient = null;

const setConnected = (connected) => {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#chatLine").show();
  } else {
    $("#chatLine").hide();
  }
  $("#message").html("");
}

const connect = () => {
  stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
  stompClient.connect({}, (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/users', (userJson) => showUser(JSON.parse(userJson.body).id, JSON.parse(userJson.body).name));
  });
}

const disconnect = () => {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

const sendName = () => stompClient.send("/app/new_user", {}, JSON.stringify({'name': $("#userName").val()}))


const showUser = (userId, userName) => $("#usersTable").append("<tr><td>" + userId + "</td><td>" + userName + "</td></tr>")


$(function () {
  $("form").on('submit', (event) => {
    event.preventDefault();
  });
  $("#connect").click(connect);
  $("#disconnect").click(disconnect);
  $("#send").click(sendName);
  connect();
});
