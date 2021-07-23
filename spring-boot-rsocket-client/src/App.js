import logo from "./logo.svg";
import "./App.css";
import { useCallback, useEffect, useState } from "react";
import rsocketService from "./RSocketService";
import { Metadata } from "./Metadata";
import {
  encodeAndAddCustomMetadata,
  encodeAndAddWellKnownMetadata,
  MESSAGE_RSOCKET_ROUTING,
} from "rsocket-core";

function App() {
  const [data, setData] = useState(null);

  const handleSocket = useCallback((socket) => {
    let metadata = new Metadata();
    metadata.set(Metadata.ROUTE, "notification.stream");
    metadata.set(Metadata.AUTHENTICATION_BEARER, "Bearer 213esfesdfsdf");

    socket
      .requestStream({
        data: 10,
        metadata: metadata,
      })
      .subscribe({
        onError: (e) => console.log("error: ", e.source),
        onNext: (payload) => setData((d) => (d ? [payload, ...d] : [payload])),
        onSubscribe: (subscription) => {
          subscription.request(1000000); // set it to some max value
        },
      });
  }, []);

  useEffect(() => {
    rsocketService
      .getRSocketConnection()
      .then((socket) => handleSocket(socket))
      .catch((e) => console.log("error: ", e));
  }, [handleSocket]);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        {data && data.map((d, i) => <div key={i}>{JSON.stringify(d)}</div>)}
      </header>
    </div>
  );
}

export default App;
