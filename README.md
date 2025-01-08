<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#results">Results</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#Contributing">Contributing</a></li>
    <li><a href="#Contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project


The Price Feed Simulator is a two-part application designed to generate and display simulated stock price data in real time. It consists of a Data Generator and a Data Feed UI, working in parallel to create and visualize financial market data.

**Data Generator:**

This module generates realistic stock data for up to 10,000 tickers, including fields such as bid price, ask price, last price, and quote time (UTC). It updates the data at configurable intervals, ensuring a dynamic and diverse data feed. Updates are distributed evenly among tickers, providing a realistic simulation of market activity.

**Data Feed UI:**

A user-friendly console application that receives and displays the data from the generator. Users can query current data for specific tickers, view real-time updates, access the last 10 updates for a ticker, calculate a 5-minute moving average, or identify the top 10 tickers with the largest price changes since startup.

Together, these applications simulate a real-time stock market environment, providing a practical tool for analyzing and visualizing dynamic price movements.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Results

You only need to pay attention to the Client-Side Terminal. 

### Training, Validation, and Test Data Splits
![Initial Setup][init-vars]

This is the initial setup of the program. Input the number of tickers you want created and then the interval at which you want these tickers to be refreshed. 

### Ticker Results
![Tickers][tickers]

This is what the tickers will look like. It will be set up with realistic data and its behavior will mimic real stock data. 

### Post-Setup Options
![Options][options]

After the initial setup is done, it will ask you what you would like to see. You can specify any of the options and it will be displayed in the same format as above. As long as the processes are alive, the tickers will be continuously updated in the background at the interval you specified in the beginning. 


<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps. You can find testing instructions in the repository as a pdf file. 

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/rohanvc/Basic-Language-Model-with-Multi-Headed-Attention-Blocks.git
   ```
2. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Rohan Chaturvedula - [Linkedin](https://www.linkedin.com/in/rohan-chaturvedula/) - rchat10@gmail.com


<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- Saved Locations for images -->
[init-vars]: images/Initial_Vars.png
[options]: images/Options.png
[tickers]: images/Tickers.png



