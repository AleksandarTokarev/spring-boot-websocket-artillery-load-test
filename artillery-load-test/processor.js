let messages = ["message1", "message2", "message3"]

function generateMessageData (userContext, events, done) {
    userContext.vars.message = messages[Math.floor(Math.random() * messages.length)];
    return done()
}

// dummy example tokens
let tokens = [
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJWbGFkaSIsImlhdCI6MTUxNjIzOTAyMn0.HW5TwqKuFdRfw7Rotqha4uxi-mHNR-PhqhxnvwsDT8o",
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJCb3lrbyIsImlhdCI6MTUxNjIzOTAyMn0.Z6GAZJTyX8OfKVTtKFJ_5KPhDy9Jkkw39UZOhvCP1GQ",
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBbGV4IiwiaWF0IjoxNTE2MjM5MDIyfQ.kelW9cqDad7RbsH2wJbY2nJpc0WjNOnDJ8g4o_iayRU"
]

// only for metrics purposes/can be used for debugging logging purposes
function enableMessageLogging(userContext, events, next) {
    userContext.ws.on(`message`, (message) => {
        events.emit('counter', 'websocket.messages_received', 1);
        events.emit('rate', 'websocket.receive_rate');
    });
    next();
}

function connectHandler(params, context, next) {
    params.target = "ws://localhost:8080/userMessages";
    params.options.headers= {
        'x-jwt-token': tokens[Math.floor(Math.random() * tokens.length)]
    }
    next();
}

module.exports = {
    generateMessageData,
    enableMessageLogging,
    connectHandler
};
