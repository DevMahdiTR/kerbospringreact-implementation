import { Steps, Spin } from 'antd';
import { useState, useEffect } from 'react';
import {
    UserOutlined,
    SafetyCertificateOutlined,
    FileProtectOutlined,
    SendOutlined,
    CheckCircleOutlined,
    LoadingOutlined,
} from '@ant-design/icons';

const { Step } = Steps;

const AuthenticationStepper = ({ loadingStep }) => {
    const [currentStep, setCurrentStep] = useState(0);

    useEffect(() => {
        let timer;
        if (currentStep < 6) {
            timer = setInterval(() => {
                setCurrentStep((prevStep) => (prevStep === 5 ? 0 : prevStep + 1));
            }, 1000);
        }
        return () => {
            clearInterval(timer);
        };
    }, [currentStep]);

    const getIcon = (step) => {
        if (step === loadingStep) {
            return <LoadingOutlined />;
        }
        switch (step) {
            case 0:
            case 3:
            case 5:
                return <UserOutlined />;
            case 1:
                return <SafetyCertificateOutlined />;
            case 2:
                return <FileProtectOutlined />;
            case 4:
                return <CheckCircleOutlined />;
            default:
                return null;
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', padding: '20px' }}>
            <Steps
                current={currentStep}
                direction="vertical"
                size="small"
                style={{
                    background: '#fff',
                    padding: '20px',
                    borderRadius: '8px',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
                    maxWidth: '300px'
                }}
            >
                <Step
                    title="Client"
                    description="Sending request to AS"
                    icon={getIcon(0)}
                />
                <Step
                    title="AS"
                    description="Verifying client and generating TGT"
                    icon={getIcon(1)}
                />
                <Step
                    title="TGT"
                    description="Sending TGT to client"
                    icon={getIcon(2)}
                />
                <Step
                    title="Client"
                    description="Sending TGT to service"
                    icon={getIcon(3)}
                />
                <Step
                    title="Service"
                    description="Verifying TGT and generating service ticket"
                    icon={getIcon(4)}
                />
                <Step
                    title="Client"
                    description="Sending service ticket to service"
                    icon={getIcon(5)}
                />
            </Steps>
        </div>
    );
};

export default AuthenticationStepper;
