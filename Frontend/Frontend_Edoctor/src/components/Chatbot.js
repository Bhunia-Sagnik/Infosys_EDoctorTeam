import React, { useState, useEffect, useRef } from "react";
import "../CSS/Chatbot.css"; // Ensure CSS is applied correctly
import { FiMessageSquare, FiX, FiArrowUp } from "react-icons/fi";

const Chatbot = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const messagesEndRef = useRef(null);

  // Predefined keywords and corresponding responses
  const keywordResponses = [
    { keywords: ["hi", "hello"], response: "Hi there! How can I assist you with your doctor appointment today?" },
    { keywords: ["online", "appointment"], response: "An online doctor appointment allows you to consult with a healthcare professional remotely via video, phone, or text." },
    { keywords: ["book", "appointment"], response: "You can book an appointment by providing your details, choosing a doctor, and selecting a time slot through our platform." },
    { keywords: ["cancel", "appointment"], response: "Yes, you can cancel your appointment anytime before the scheduled time. Please visit your appointment details page to cancel." },
    { keywords: ["available", "doctors"], response: "Go to the doctors section in the patient dashboard. Search doctors and click on get details to see available dates." },
    { keywords: ["add", "availability", "doctor"], response: "Go to the availability section in the doctor dashboard enter the start-date and end-date of your availability to add availability." },
    { keywords: ["reschedule", "appointment"], response: "To reschedule, go to your appointment details and select the option to change the date or time of your appointment." },
    { keywords: ["pay", "appointment"], response: "You can pay for your appointment using credit/debit cards, net banking, or other online payment methods available on our platform." },
    { keywords: ["miss", "appointment"], response: "If you miss your appointment, please contact our support team to reschedule or discuss the next steps." },
    { keywords: ["specific", "doctor"], response: "Yes, you can choose from a list of available doctors in your area based on specialty, availability, and ratings." },
    { keywords: ["specialties", "offer"], response: "We offer consultations in various specialties, including general medicine, dermatology, psychiatry, gynecology, pediatrics, and more." },
    { keywords: ["find", "doctor"], response: "You can search for doctors by specialty, location, or name using the search feature on our platform." },
    { keywords: ["secure", "information"], response: "Yes, your information is securely stored and processed using encryption and compliance with privacy regulations to ensure confidentiality." },
    { keywords: ["contact", "support"], response: "You can contact our customer support via email, phone, or chat available on the platform." },
    { keywords: ["doctor perspective", "advice"], response: "As a doctor, I recommend maintaining a balanced diet, exercising regularly, and scheduling routine check-ups to monitor your health." },
    { keywords: ["symptoms", "diagnose"], response: "I can provide general guidance, but for specific symptoms, please consult a doctor for an accurate diagnosis." },
    { keywords: ["precautions", "disease"], response: "Doctors recommend following hygiene protocols, staying updated with vaccinations, and avoiding exposure to contagious environments." },
    { keywords: ["bye"], response: "Goodbye! Have a great day!" },
  ];

  // Find the best response based on keywords
  const getResponse = (message) => {
    const normalizedMessage = message.trim().toLowerCase();

    for (let { keywords, response } of keywordResponses) {
      if (keywords.some((keyword) => normalizedMessage.includes(keyword))) {
        return response;
      }
    }
    return "Sorry, I don't have an answer for that. Contact us at support@gmail.com.";
  };

  // Handle sending a message
  const handleSend = () => {
    if (input.trim() === "") return;

    const userMessage = input.trim();

    // Update messages with user's message
    setMessages((prevMessages) => [...prevMessages, { type: "user", text: userMessage }]);
    setInput(""); // Clear input field

    // Simulate bot response with a delay
    setTimeout(() => {
      const botResponse = getResponse(userMessage);
      setMessages((prevMessages) => [...prevMessages, { type: "bot", text: botResponse }]);
    }, 1000); // 1-second delay
  };

  // Scroll to the bottom of the chat when messages update
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="chatbot-container">
      <div className={`chatbot ${isOpen ? "open" : ""}`}>
        <div className="chatbot-header">
          <h3>DocBot</h3>
          <button onClick={() => setIsOpen(false)}>
            <FiX />
          </button>
        </div>
        <div className="chatbot-messages">
          {messages.map((msg, index) => (
            <div key={index} className={`chatbot-message ${msg.type}`}>
              {msg.text}
            </div>
          ))}
          <div ref={messagesEndRef} />
        </div>
        <div className="chatbot-input">
          <input
            type="text"
            placeholder="Type your message..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={(e) => e.key === "Enter" && handleSend()}
          />
          <button onClick={handleSend}>
            <FiArrowUp />
          </button>
        </div>
      </div>
      {!isOpen && (
        <button className="chatbot-toggle" onClick={() => setIsOpen(true)}>
          <FiMessageSquare /> 
        </button>
      )}
    </div>
  );
};

export default Chatbot;
