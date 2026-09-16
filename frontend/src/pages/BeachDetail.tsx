import "../App.css";
import { useParams } from "react-router-dom";
import { BeachHeader } from "../components/BeachHeader";
import BeachStats from "../components/BeachStats";
import { useState, useEffect } from "react";
import WhyRating from "../components/WhyRating";
import GoodStuff from "../components/GoodStuff";
import BadStuff from "../components/BadStuff";

interface BeachData {
    id: number;
    name: string;
    rating: number;
    waveHeight: number;
    wavePeriod: number;
    windSpeed: number;
    windDirection: string;
    tide: number;
    waterTemp: number;
    weather: string;
    reasoning: string;
    goodStuff: string;
    badStuff: string;
}

export default function BeachDetail() {
    const { beachName } = useParams<{ beachName: string }>(); //save the recieved parameter as a variable named beachName
    const [currentBeach, setCurrentBeach] = useState<BeachData | null>(null); //state variable to hold the current beach data, can either hold a valid beach data object or be null.
    const [isLoading, setIsLoading] = useState<boolean>(true); //waits for the data to be fetched before rendering the page, initially set to true, while true can show a placeholder like Loading...
    useEffect(() => {
        if (!beachName) return;

        const controller = new AbortController();
        const targetUrl = `${import.meta.env.VITE_API_BASE_URL}/beaches?name=${encodeURIComponent(beachName)}`;

        fetch(targetUrl, { signal: controller.signal })
            .then((response) => response.json())
            .then((data: BeachData) => {
                setCurrentBeach(data);
            })
            .catch((error) => {
                if (error.name !== "AbortError") {
                    console.error("Error fetching beach data:", error);
                }
            })
            .finally(() => {
                setIsLoading(false);
            });

        return () => {
            controller.abort();
        };
    }, [beachName]);

    return (
        <div className="beach-page-grid">
            <div className="beach-header">
                {isLoading === true ? (
                    <p>Loading...</p>
                ) : (
                    <BeachHeader
                        beachName={currentBeach?.name ?? "Unknown Beach"}
                        beachRating={currentBeach?.rating ?? 0}
                    />
                )}
            </div>
            <BeachStats
                waveHeight={currentBeach?.waveHeight ?? 0}
                wavePeriod={currentBeach?.wavePeriod ?? 0}
                windSpeed={currentBeach?.windSpeed ?? 0}
                windDirection={currentBeach?.windDirection ?? "Unknown"}
                tide={currentBeach?.tide ?? 0}
                waterTemp={currentBeach?.waterTemp ?? 0}
                weather={currentBeach?.weather ?? "Unknown"}
            />
            <WhyRating
                rating={currentBeach?.rating ?? 0}
                reasoning={currentBeach?.reasoning ?? "No Reasoning Available"}
            />
            <GoodStuff goodStuff={currentBeach?.goodStuff ?? "N/A"} />
            <BadStuff badStuff={currentBeach?.badStuff ?? "N/A"} />
        </div>
    );
}
