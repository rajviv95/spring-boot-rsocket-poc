import logo from "./logo.svg";
import "./App.css";
import { useCallback, useEffect, useState } from "react";
import rsocketService from "./RSocketService";
import { Metadata as M1, JsonMetadataSerializer } from "./Metadata";
import { MAX_STREAM_ID } from "rsocket-core";

function App() {
  const [data, setData] = useState(null);

  const handleSocket = useCallback((socket) => {
    let m1 = new M1();
    m1.set(M1.ROUTE, "notification.stream");
    m1.set(M1.AUTHENTICATION_BEARER, "35d119e9-ac02-488d-8940-52a1d6a5be50");

    console.log(m1);

    socket
      .requestStream({
        metadata: m1,
      })
      .subscribe({
        onError: (e) => console.log("error2 : ", e.source),
        onNext: (payload) => setData((d) => (d ? [payload, ...d] : [payload])),
        onSubscribe: (subscription) => {
          subscription.request(MAX_STREAM_ID); // set it to some max value
        },
      });
  }, []);

  useEffect(() => {
    rsocketService
      .getRSocketConnectionSubscribe()
      .then((socket) => {
        console.log("setup done");
        handleSocket(socket);
      })
      .catch((e) => console.log("error1: ", e));
  }, [handleSocket]);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        {data &&
          data.map((d, i) => <div key={i}>{JSON.stringify(d.data)}</div>)}
      </header>
    </div>
  );
}

export default App;
