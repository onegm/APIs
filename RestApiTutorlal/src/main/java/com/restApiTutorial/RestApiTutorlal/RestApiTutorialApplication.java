package com.restApiTutorial.RestApiTutorlal;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class RestApiTutorialApplication {

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		Transcript transcript = new Transcript();
		transcript.setAudio_url("https://github.com/onegm/APIs/blob/7a61245a121aa133d5e739c980926565cae2a6aa/Hello.mp3?raw=true");

		Gson gson = new Gson();
		String jsonRequest = gson.toJson(transcript);

		System.out.println(jsonRequest);

		HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript"))
				.header("authorization", Constants.APIKEY)
				.POST(BodyPublishers.ofString(jsonRequest))
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());

		System.out.println(postResponse.body());

		transcript = gson.fromJson(postResponse.body(), Transcript.class);

		System.out.println(transcript.getId());

		HttpRequest getRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
				.header("authorization", Constants.APIKEY)
				.build();


		while(transcript.getStatus().equals("queued") || transcript.getStatus().equals("processing") ){
			HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
			transcript = gson.fromJson(getResponse.body(), Transcript.class);
			System.out.println(transcript.getStatus());

			Thread.sleep(1000);
		}

		System.out.println(transcript.getText());

	}

}
