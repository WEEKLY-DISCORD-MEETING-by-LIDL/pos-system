export const createOrderStyle = {

    page: {
        width: "100%",
        height: "100vh",
        display: "grid",
        gridTemplateAreas: `
            "sidebar body"
        `,
        gridTemplateColumns: "30% 1fr",
        gap: "8px"
    },

    itemListSidebar: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        gridArea: "sidebar",
        borderRadius: 1,
        borderStyle: "solid",
        borderColor: '#000000'
    },

    selectionBox: {
        gridArea: "body",
        borderRadius: 1,
        borderStyle: "solid",
        borderColor: '#000000'
    },

    itemList: {
        height: "60vh",
        overflowY: "auto",
        borderRadius: 1,
        borderStyle: "solid",
        borderColor: '#000000'
    },

    createOrderButton: {
        marginTop: "8px", // Add some spacing above the button
        padding: "8px 16px",
        backgroundColor: "#007bff",
        color: "#ffffff",
        border: "none",
        borderRadius: "4px",
        cursor: "pointer",
    },
}