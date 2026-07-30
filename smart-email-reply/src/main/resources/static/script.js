const BACKEND_URL = "";

async function generateReply() {

    const emailContent = document.getElementById("emailContent").value;
    const tone = document.getElementById("tone").value;

    const replyBox = document.getElementById("reply");
    const subjectBox = document.getElementById("subject");
    const loading = document.getElementById("loading");

    if (emailContent.trim() === "") {
        alert("Please enter email content");
        return;
    }

    loading.innerText = "Generating AI reply...";

    replyBox.value = "";
    subjectBox.value = "";

    try {

        const response = await fetch(BACKEND_URL + "/api/email/reply", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                emailContent: emailContent,
                tone: tone
            })
        });

        if (!response.ok) {
            throw new Error("Failed to generate reply");
        }

        const data = await response.json();

        subjectBox.value = data.subject;
        replyBox.value = data.reply;

        loading.innerText = "Reply generated successfully";

    } catch (error) {

        console.error(error);
        loading.innerText = "Error generating reply";

    }
}

function copyReply() {

    const reply = document.getElementById("reply").value;

    if (reply.trim() === "") {
        alert("No reply available");
        return;
    }

    navigator.clipboard.writeText(reply)
        .then(() => {
            alert("Reply copied!");
        })
        .catch(() => {
            alert("Failed to copy reply.");
        });
}